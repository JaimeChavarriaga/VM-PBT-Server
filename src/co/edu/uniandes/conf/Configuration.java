package co.edu.uniandes.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class Configuration {
	private int port;
	private String virtualMachinesHome;
	private String virtualBoxHome;
	private String labelLogFile;
	private String pathLog;
	private String logFileName;

	public Configuration(String filename) {
		Properties p = new Properties();
		InputStream is = null;

		try {
			is = new FileInputStream(filename);

			p.load(is);

			port = Integer.parseInt(p.getProperty("port"));
			virtualMachinesHome = p.getProperty("virtualMachinesHome");
			virtualBoxHome = p.getProperty("virtualBoxHome");
			labelLogFile = p.getProperty("labelLogFile");
			pathLog = p.getProperty("pathLog");
			logFileName = p.getProperty("logFileName");		

			} catch (IOException e) {
		}
	}
	
	/**
	 * This method returns the port number.
	 * 
	 * @return int The port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * This method returns the directory path where the virtual machines stored are.
	 * 
	 * @return String The directory path of the virtual machines repository.
	 */
	public String getVirtualMachinesHome() {
		return virtualMachinesHome;
	}

	/**
	 * This method returns the directory path where VirtualBox installed is.
	 * 
	 * @return String The directory path of VirtualBox.
	 */
	public String getVirtualBoxHome() {
		return virtualBoxHome;
	}

	/**
	 * This method returns the label of each entry in the log file.
	 * 
	 * @return String The label.
	 */
	public String getLabelLogFile() {
		return labelLogFile;
	}

	/**
	 * This method returns the directory path where the log file will be stored.
	 * 
	 * @return String The pathLog.
	 */
	public String getPathLog() {
		return pathLog;
	}

	/**
	 * This method returns the log file name.
	 * 
	 * @return String The log file name.
	 */
	public String getLogFileName() {
		return logFileName;
	}
}
