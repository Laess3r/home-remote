package at.dahuawa.homecontrolapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogFile implements Serializable {
	private static final long serialVersionUID = 1L;
	final List<LogEntry> logEntries;

	public LogFile() {
		logEntries = new ArrayList<LogEntry>();
	}

	public List<LogEntry> getLogFile() {
		return logEntries;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		for (LogEntry e : getLogFile()) {
			b.append(e.getTimeStamp() + ": " + e.getText());
			b.append("\n");
		}

		return b.toString();
	}
}