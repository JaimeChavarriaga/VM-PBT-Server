package co.edu.uniandes.resources;

import co.edu.uniandes.model.HardwareProfile;
import co.edu.uniandes.util.Constants;

/**
 * Class to represent a Virtual Machine
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public abstract class VirtualMachine {
		
	/**
	 * Name in Hypervisor of Virtual Machine
	 */
	private String name;
	
	/**
	 * Time of SignUp in seconds
	 */
	private double[] timesSignUp;
	
	/**
	 * Actual Hardware Profile
	 */
	private HardwareProfile profile;
	
	/**
	 * Constructor
	 * @param name of Virtual Machine
	 */
	public VirtualMachine(String name, int nTests) {
		this.name = name;
		this.timesSignUp = new double[nTests];
	}
	
	//------------------------------------------------------
	//	Methods
	//------------------------------------------------------
	/**
	 * Turn on the Virtual Machine
	 */
	public abstract void vmTurnOn();
	
	/**
	 * Turn off the Virtual Machine
	 */
	public abstract void vmTurnOff();
	
	/**
	 * Modify the Virtual Machine with the Actual Profile
	 */
	public abstract void vmModifyHardwareProfile();

	public void reinitialize() {
		this.timesSignUp = new double[this.timesSignUp.length];
	}
	
	//------------------------------------------------------
	//	Getters and Setters
	//------------------------------------------------------
	public void setTimeSignUpByTest(long startTime, long endTime, int test) {
		timesSignUp[test] = (endTime-startTime)/Constants.MILLON;
	}
	
	public double getTimeByTest(int test) {
		return timesSignUp[test];
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HardwareProfile getProfile() {
		return profile;
	}

	public void setProfile(HardwareProfile profile) {
		this.profile = profile;
		this.vmModifyHardwareProfile();
	}

	public double[] getTimesSignUp() {
		return timesSignUp;
	}

	public void setTimesSignUp(double[] timesSignUp) {
		this.timesSignUp = timesSignUp;
	}
}
