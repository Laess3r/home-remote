package at.dahu4wa.homecontrol.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Log file entry that is transfered between server and client
 * 
 * @author Stefan Huber
 */
@XmlRootElement
public class LogEntry {

	final Date timeStamp;
	final String logEntry;

	public LogEntry(String logEntry) {
		this.timeStamp = new Date(System.currentTimeMillis());
		this.logEntry = logEntry;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getLogEntry() {
		return logEntry;
	}
}