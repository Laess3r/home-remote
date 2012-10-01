package at.dahu4wa.homecontrol.serialcommunication;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.util.TooManyListenersException;

/**
 * TODO Comment me
 * 
 * @author Stefan Huber
 */
public class SerialCommunication {

	private SerialReader ser = null;

	public SerialCommunication() throws IOException {
		super();
	}

	/**
	 * Establish connection between the computer and the arduino board
	 */
	public SerialPort connect(String portName) throws Exception {

		SerialPort serialPort = null;

		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {

			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				serialPort.disableReceiveTimeout();
				serialPort.enableReceiveThreshold(1);

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}

		return serialPort;
	}

	/**
	 * Registers an event at given serial port
	 * @param jTextArea 
	 */
	public void registerEventListener(SerialPort serialPort, SerialReaderCallback callback)
			throws IOException, TooManyListenersException {
		
		ser = new SerialReader(serialPort.getInputStream(), callback);

		serialPort.addEventListener(ser);
		serialPort.notifyOnDataAvailable(true);

	}

}
