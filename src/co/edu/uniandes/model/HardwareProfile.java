package co.edu.uniandes.model;

/**
 * Class to represent a Hardware Profile
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class HardwareProfile {

	/**
	 * Name of Hardware Profile
	 */
	private String name;
	
	/**
	 * Amount RAM in GB
	 */
	private int RAM;
	
	/**
	 * Number of cores of the Machine
	 */
	private int cores;
	
	/**
	 * Constructor
	 * @param RAM in the Hardware Profile
	 * @param cores in the Hardware Profile
	 */
	public HardwareProfile(String name, int RAM, int cores) {
		this.name = name;
		this.RAM = RAM;
		this.cores = cores;
	}
	
	//------------------------------------------------------
	//	Methods
	//------------------------------------------------------
	public String toString() {
		return "["+name+":"+RAM+" GB - "+cores+" cores]";
	}
	
	//------------------------------------------------------
	//	Getters and Setters
	//------------------------------------------------------
	public int getRAM() {
		return RAM;
	}

	public int getCores() {
		return cores;
	}
	
}
