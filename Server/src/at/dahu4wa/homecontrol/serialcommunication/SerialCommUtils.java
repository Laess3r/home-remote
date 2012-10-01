package at.dahu4wa.homecontrol.serialcommunication;

import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * Helper class for making serial communications
 * 
 * @author Stefan Huber
 */
public class SerialCommUtils {

	private static SerialCommunication serialComm;

	private Thread sendThread = null;

	public SerialPort initialize(String port) throws Exception {

		SerialPort serialPort = null;

		try {
			serialComm = new SerialCommunication();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		serialPort = serialComm.connect(port);

		return serialPort;
	}

	public void registerEventListener(SerialPort serialPort, SerialReaderCallback callback) throws IOException {

		try {
			serialComm.registerEventListener(serialPort, callback);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Send given message to given outputStream
	 */
	public void sendToSerialPort(OutputStream output, byte[] messageToSend) {

		sendThread = new Thread(new SerialWriter(output, messageToSend));
		sendThread.start();
	}
}
