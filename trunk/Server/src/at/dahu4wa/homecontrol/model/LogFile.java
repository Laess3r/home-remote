package at.dahu4wa.homecontrol.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Log file object 
 * 
 * @author Stefan Huber
 */
@XmlRootElement
public class LogFile {

	final List<LogEntry> logEntries;

	public LogFile() {
		logEntries = new ArrayList<LogEntry>();
		logEntries.add(new LogEntry("Logfile created"));
	}

	public List<LogEntry> getLogFile() {
		return logEntries;
	}
	
	public void log(String logEntry){
		logEntries.add(new LogEntry(logEntry));
	}
}