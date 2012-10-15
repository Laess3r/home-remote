package at.dahu4wa.homecontrol.maincontrol;

/**
 * Helper to detect the Serial port depending on the OS
 * This is useful in order to test in on Windows and deploy it on Unix without changing properties
 * 
 * @author Stefan Huber
 *
 * OS Detection code source:
 * http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 */
public class OSDetector {

	public static String getSerialPort() {
		
		if (isWindows()) {
			return "COM5";			//TODO get this from properties
		} else if (isUnix()) {
			return "/dev/ttyUSB0";	//TODO get this from properties
		}
		return null;
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}
}
