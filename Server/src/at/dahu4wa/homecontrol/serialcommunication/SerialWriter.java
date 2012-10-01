package at.dahu4wa.homecontrol.serialcommunication;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes data to output stream (in a new thread)
 * 
 * @author Stefan Huber
 */
public class SerialWriter implements Runnable {
	OutputStream out;
	byte[] data;

	public SerialWriter(OutputStream out, byte[] data) {
		this.out = out;
		this.data = data;
	}

	public void run() {
		try {
			this.out.write(data);
			this.out.flush();
			this.out.close();
		} catch (IOException e) {
			System.err.println("IOException "+e);
		}
	}
}