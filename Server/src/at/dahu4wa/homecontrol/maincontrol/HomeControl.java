package at.dahu4wa.homecontrol.maincontrol;

import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import at.dahu4wa.homecontrol.model.Plug;
import at.dahu4wa.homecontrol.serialcommunication.SerialCommUtils;
import at.dahu4wa.homecontrol.serialcommunication.SerialReaderCallback;

/**
 * Main Controller for Home Control
 * 
 * TODO Push the plugs into a h2 database
 * 
 * TODO Security
 * 
 * @author Stefan Huber
 */
public class HomeControl {

	/*
	 * Windows: "COM5" 
	 * Linux: "/dev/ttyUSB0" 
	 */
	private final static String SERIAL_PORT_NAME = "/dev/ttyUSB0";

	private static SerialPort serialPort;
	private static OutputStream serialOutStream;
	private static SerialCommUtils serialCommUtils;

	private static Plug PLUG_A = new Plug('A', "Entertaiment", false);
	private static Plug PLUG_B = new Plug('B', "Leuchtschiene", false);
	private static Plug PLUG_C = new Plug('C', "E-Piano", false);
	private static Plug PLUG_D = new Plug('D', "Schreibtisch", false);

	private boolean arduinoAnswered = false;

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
			// case 'a':
			return PLUG_A;
		case 'B':
			// case 'b':
			return PLUG_B;
		case 'C':
			// case 'c':
			return PLUG_C;
		case 'D':
			// case 'd':
			return PLUG_D;
		default:
			return null;
		}
	}

	public List<Plug> getAllPlugs() {
		List<Plug> allPlugs = new ArrayList<Plug>();

		allPlugs.add(PLUG_A);
		allPlugs.add(PLUG_B);
		allPlugs.add(PLUG_C);
		allPlugs.add(PLUG_D);
		return allPlugs;
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

		return plugToUpdate;
	}

	public void init() {
		try {

			serialCommUtils = new SerialCommUtils();
			serialPort = serialCommUtils.initialize(SERIAL_PORT_NAME);
			serialCommUtils.registerEventListener(serialPort, new SerialReaderCallback() {

				@Override
				public void onFinish(String incomingMessage) {
					arduinoAnswered = true;
				}
			});

			serialOutStream = serialPort.getOutputStream();

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

		long time = System.currentTimeMillis(); // wait 5 seconds or until
												// arduino finished
		while (!arduinoAnswered && (System.currentTimeMillis() - time < 5000)) {
			System.out.print("");
		}
		arduinoAnswered = false;
	}

}
