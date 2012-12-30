package at.dahuawa.homecontrolapp.model;

import java.io.Serializable;

public class LogEntry implements Serializable{
	private static final long serialVersionUID = 1L;
	private String timeStamp;
	private String text;

	public LogEntry(String logEntry, String timeStamp) {

		String a = timeStamp.substring(0, 10);
		String b = timeStamp.substring(11, 19);

		this.timeStamp = a + " - " + b;
		this.text = logEntry;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getText() {
		return text;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setText(String logEntry) {
		this.text = logEntry;
	}
}