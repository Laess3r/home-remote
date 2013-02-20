package at.dahuawa.homecontrolng.model;

import java.util.Date;

/**
 * Log file entry that is transfered between server and client
 * 
 * @author Stefan Huber
 */
public class LogEntry {

	private Date timeStamp;
	private String text;

	public LogEntry() {
		this.timeStamp = new Date(System.currentTimeMillis());
		this.text = "empty";
	}
	
	public LogEntry(String logEntry) {
		this.timeStamp = new Date(System.currentTimeMillis());
		this.text = logEntry;
	}
	
	public LogEntry(String logEntry, String timestamp) {
		this.timeStamp = ISO8601.toDate(timestamp);
		this.text = logEntry;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getText() {
		return text;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setText(String logEntry) {
		this.text = logEntry;
	}
}