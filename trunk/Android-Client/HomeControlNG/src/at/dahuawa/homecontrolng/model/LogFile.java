package at.dahuawa.homecontrolng.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Log file object
 * 
 * @author Stefan Huber
 */
public class LogFile {

	final List<LogEntry> logEntries;

	public LogFile() {
		logEntries = new ArrayList<LogEntry>();
		logEntries.add(new LogEntry("Logfile created"));
	}

	public List<LogEntry> getLogFile() {
		return logEntries;
	}

	public void log(String logEntry) {
		logEntries.add(new LogEntry(logEntry));

		while (logEntries.size() > 100) {
			logEntries.remove(0);
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (LogEntry entry : logEntries) {
			str = str + "\n\n" + entry.toString();
		}
		return str;

	}
}