package at.dahu4wa.homecontrol.serialcommunication;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Is an event listener for incoming messages Reads incoming messages from the
 * serial port
 * 
 * @author Stefan Huber
 */
public class SerialReader implements SerialPortEventListener {
	private InputStream in;
	private BufferedReader input;
	private String inputLine;
	public Properties properties;
	private final SerialReaderCallback callback;

	public SerialReader(InputStream in, SerialReaderCallback callback) {
		this.in = in;
		this.properties = new Properties();
		this.callback = callback;
	}

	public void serialEvent(SerialPortEvent arg0) {

		input = new BufferedReader(new InputStreamReader(in));
		
		try {

			while ((inputLine = input.readLine()) != null) {
				callback.onFinish(inputLine);
			}

	
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
