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