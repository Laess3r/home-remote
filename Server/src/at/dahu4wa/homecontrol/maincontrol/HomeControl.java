package at.dahu4wa.homecontrol.maincontrol;

import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.dahu4wa.homecontrol.model.LogEntry;
import at.dahu4wa.homecontrol.model.LogFile;
import at.dahu4wa.homecontrol.model.MediaStatus;
import at.dahu4wa.homecontrol.model.Plug;
import at.dahu4wa.homecontrol.model.TempSensor;
import at.dahu4wa.homecontrol.serialcommunication.SerialCommUtils;
import at.dahu4wa.homecontrol.serialcommunication.SerialReaderCallback;
import de.hotware.hotsound.audio.data.BasicPlaybackAudioDevice;
import de.hotware.hotsound.audio.player.BasicPlaybackSong;
import de.hotware.hotsound.audio.player.MusicPlayerException;
import de.hotware.hotsound.audio.player.StreamMusicPlayer;

/**
 * Main Controller for Home Control
 * 
 * TODO Push the plugs into a h2 database TODO Names should be set via client
 * config
 * 
 * @author Stefan Huber
 */
public class HomeControl {

	private static LogFile logFile;

	private static SerialPort serialPort;
	private static OutputStream serialOutStream;
	private static SerialCommUtils serialCommUtils;

	private static Plug PLUG_A = new Plug('A', "Entertaiment", false);
	private static Plug PLUG_B = new Plug('B', "Leuchtschiene", false);
	private static Plug PLUG_C = new Plug('C', "E-Piano", false);
	private static Plug PLUG_D = new Plug('D', "Schreibtisch", false);
	private static Plug PLUG_E = new Plug('E', "Ikea Dioder", false);
	private static Plug PLUG_X = new Plug('X', "Ambilight", false);

	private static TempSensor SENSOR_A = new TempSensor('A', "Innen ", 0);
	private static TempSensor SENSOR_B = new TempSensor('B', "Aussen", 0);

	private static StreamMusicPlayer player = new StreamMusicPlayer();

	private boolean updateFinished = false;

	public HomeControl() {
		init();
	}

	/**
	 * Return the requested Plug
	 * 
	 * @param plugId
	 *            - A,B,C or D
	 * @return the plug
	 */
	public Plug getPlugById(char plugId) {
		switch (plugId) {
		case 'A':
			return PLUG_A;
		case 'B':
			return PLUG_B;
		case 'C':
			return PLUG_C;
		case 'D':
			return PLUG_D;
		case 'E':
			return PLUG_E;
		case 'X':
			return PLUG_X;
		default:
			return null;
		}
	}

	public List<Plug> getAllPlugs() {
		return Arrays.asList(PLUG_A, PLUG_B, PLUG_C, PLUG_D, PLUG_E, PLUG_X);
	}

	/**
	 * Update the plug enabled status
	 * 
	 * @param plugId
	 * @return the updated Plug
	 */
	public Plug updatePlugStatus(char plugId, boolean isEnabled) {
		Plug plugToUpdate = getPlugById(plugId);
		plugToUpdate.setEnabled(isEnabled);

		updatePlugHw(plugToUpdate);
		// logFile.log("Plug " + plugId + " set to: " + isEnabled);
		return plugToUpdate;
	}

	public void init() {
		try {

			Timer t = new Timer("Updating LCD", true);
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					updateAllTempSensors();
				}
			}, 1000, 30000);

			logFile = new LogFile();

			serialCommUtils = new SerialCommUtils();
			serialPort = serialCommUtils.initialize(OSDetector.getSerialPort());
			serialCommUtils.registerEventListener(serialPort, new SerialReaderCallback() {

				@Override
				public void onFinish(String incomingMessage) {

					if (incomingMessage.startsWith("X")) {
						String[] splitTemp = incomingMessage.split("T");
						if (splitTemp[1].startsWith("A"))
							SENSOR_A.setTempValue(Float.parseFloat(splitTemp[1].substring(1)));
						if (splitTemp[2].startsWith("H"))
							SENSOR_A.setHumidity(Float.parseFloat(splitTemp[2].substring(1)));
						if (splitTemp[3].startsWith("B"))
							SENSOR_B.setTempValue(Float.parseFloat(splitTemp[3].substring(1)));
					} else if (incomingMessage.startsWith("TA")) {
						String[] splitTemp = incomingMessage.split("T");
						SENSOR_A.setTempValue(Float.parseFloat(splitTemp[1].substring(1)));
						SENSOR_A.setHumidity(Float.parseFloat(splitTemp[2].substring(1)));
					} else if (incomingMessage.startsWith("TB")) {
						SENSOR_B.setTempValue(Float.parseFloat(incomingMessage.substring(2)));
					}
					updateFinished = true;
				}
			});

			serialOutStream = serialPort.getOutputStream();
			logFile.log("Home Control initialized successfully!");
			System.out.println("\n == Home Control initialized successfully! ==\n---------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToBoard(byte[] bytes) {
		serialCommUtils.sendToSerialPort(serialOutStream, bytes);
	}

	public void updatePlugHw(Plug plug) {
		String lcdString = plug.getName() + "\n" + (plug.isEnabled() ? "         - EIN -" : "         - AUS -");
		byte[] lcdMessage = lcdString.getBytes();
		int messageSize = lcdMessage.length + 4;
		byte[] messageToSend = new byte[messageSize];

		messageToSend[0] = new Integer(1).byteValue();
		messageToSend[1] = (byte) plug.getId();
		messageToSend[2] = new Integer(plug.isEnabled() ? 1 : 0).byteValue();

		for (int i = 0; i < lcdMessage.length; i++) {
			messageToSend[3 + i] = lcdMessage[i];
		}
		messageToSend[messageSize - 1] = '\0';
		serialCommUtils.sendToSerialPort(serialOutStream, messageToSend);

		logFile.log(lcdString);

		// wait 5 seconds or until arduino finished
		long time = System.currentTimeMillis();
		while (!updateFinished && (System.currentTimeMillis() - time < 5000)) {
			System.out.print("");
		}
		updateFinished = false;

		Timer t = new Timer("Updating LCD");
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				updateAllTempSensors();
			}
		}, 5000);

	}

	public TempSensor getTempSensorById(char id) {

		switch (id) {
		case 'A':
			updateTempSensor(SENSOR_A);
			return SENSOR_A;
		case 'B':
			updateTempSensor(SENSOR_B);
			return SENSOR_B;
		default:
			return null;
		}
	}

	public List<TempSensor> getAllTempSensors() {
		updateAllTempSensors();
		return Arrays.asList(SENSOR_A, SENSOR_B);
	}

	public void updateTempSensor(TempSensor tempSensor) {

		byte[] lcdMessage = (getLCDMessage()).getBytes();
		int messageSize = lcdMessage.length + 3;
		byte[] messageToSend = new byte[messageSize];

		messageToSend[0] = new Integer(2).byteValue();
		messageToSend[1] = (byte) tempSensor.getId();

		for (int i = 0; i < lcdMessage.length; i++) {
			messageToSend[2 + i] = lcdMessage[i];
		}
		messageToSend[messageSize - 1] = '\0';
		serialCommUtils.sendToSerialPort(serialOutStream, messageToSend);

		long time = System.currentTimeMillis();
		while (!updateFinished && (System.currentTimeMillis() - time < 5000)) {
			System.out.print("");
		}
		updateFinished = false;
	}

	public void updateAllTempSensors() {
		byte[] lcdMessage = (getLCDMessage()).getBytes();
		int messageSize = lcdMessage.length + 3;
		byte[] messageToSend = new byte[messageSize];

		messageToSend[0] = new Integer(2).byteValue();
		messageToSend[1] = (byte) 'X';

		for (int i = 0; i < lcdMessage.length; i++) {
			messageToSend[2 + i] = lcdMessage[i];
		}
		messageToSend[messageSize - 1] = '\0';
		serialCommUtils.sendToSerialPort(serialOutStream, messageToSend);

		long time = System.currentTimeMillis();
		while (!updateFinished && (System.currentTimeMillis() - time < 5000)) {
			System.out.print("");
		}
		updateFinished = false;
	}

	private String getLCDMessage() {
		
		StringBuilder b = new StringBuilder();
		
		b.append("In: ");
		b.append(SENSOR_A.getTempValue());
		b.append("'C ");
		b.append(SENSOR_A.getHumidity());
		b.append( "%\n");
		b.append("Out: ");
		b.append(SENSOR_B.getTempValue());
		b.append("'C");
		
		return b.toString();
	}

	public void sendTextToLCD(String text) {
		byte[] lcdMessage = text.getBytes();
		int messageSize = lcdMessage.length + 2;
		byte[] messageToSend = new byte[messageSize];

		messageToSend[0] = new Integer(0).byteValue();

		for (int i = 0; i < lcdMessage.length; i++) {
			messageToSend[1 + i] = lcdMessage[i];
		}
		messageToSend[messageSize - 1] = '\0';
		serialCommUtils.sendToSerialPort(serialOutStream, messageToSend);
	}

	public List<LogEntry> getLogEntries() {
		return logFile.getLogFile();
	}

	public MediaStatus getStreamStatus() {
		return new MediaStatus(!player.isStopped(), "");
	}

	public MediaStatus stopPlaying() {

		if (player.isStopped()) {
			String information = "Player has already been stopped!";
			logFile.log(information);
			sendTextToLCD(information);
			return new MediaStatus(false, information);
		}
		try {
			player.stop();
		} catch (MusicPlayerException e) {
			logFile.log(e.getLocalizedMessage() + "\n" + e.getStackTrace());
			sendTextToLCD(e.getMessage());
			return new MediaStatus(false, e.getMessage());
		}
		String information = "Player has been stopped";
		sendTextToLCD(information);
		return new MediaStatus(false, information);
	}

	public MediaStatus setStreamUrl(String url) {
		sendTextToLCD("putting url\n" + url);
		logFile.log("setting url: " + url);
		try {
			player.insert(new BasicPlaybackSong(new URL(url)), new BasicPlaybackAudioDevice());
			logFile.log("url inserted");
			player.start();
			logFile.log("player started");
		} catch (Exception e) {
			sendTextToLCD(e.getMessage());
			logFile.log(e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.getStackTrace());
			System.out.println(e);
			return new MediaStatus(false, e.getMessage());
		}

		sendTextToLCD(url);
		logFile.log("Now playing...\n" + url);
		return new MediaStatus(true, "Now playing...");
	}
}
