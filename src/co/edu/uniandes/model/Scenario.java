package co.edu.uniandes.model;

import java.util.ArrayList;

import co.edu.uniandes.util.Constants;

/**
 * Class to represent a Scenario
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class Scenario {

	/**
	 * Number of Tests to execute in this Scenario
	 */
	private int numberTests;
	
	/**
	 * Time used executing Benchmarking aplication in Seconds.
	 */
	private double[] times;
	
	/**
	 * Start Time of Execution
	 */
	private long [] startTimes;
	
	/**
	 * End Time of Execution
	 */
	private long [] endTimes;
	
	/**
	 * List of Hardware Profiles in Scenario
	 */
	private ArrayList<HardwareProfile> profiles;
	
	/**
	 * Constructor
	 * @param nTest Number of test to execute this scenario.
	 * @param nProfiles Profiles of the Scenario
	 */
	public Scenario(ArrayList<HardwareProfile> nProfiles, int nNumberTests){
		this.profiles = nProfiles;
		this.numberTests = nNumberTests;
		this.times = new double[this.numberTests];
		this.startTimes = new long[this.numberTests];
		this.endTimes = new long[this.numberTests];
	}

	//------------------------------------------------------
	//	Methods
	//------------------------------------------------------
	/**
	 * Set time in the array according to the test executed.
	 */
	public void setTimebyTest(long startTime, long endTime, int test) {
		startTimes[test] = startTime;
		endTimes[test] = endTime;
		times[test] = (endTime-startTime)/Constants.MILLON;
	}
	
	/**
	 * Average time of Scenario
	 * @return
	 */
	public double averageTime() {
		double suma = 0;
		for(int i=0; i<numberTests; i++) {
			suma = suma + times[i];
		}
		return suma/((double) numberTests);
	}
	
	/**
	 * Return the time in the t position
	 * @param t position or t test
	 * @return Time of that Test
	 */
	public double getTimeByTest(int t) {
		return times[t];
	}
	
	/**
	 * Return the number of machines in the scenario
	 * @return number of profiles
	 */
	public int getSizeScenario() {
		return profiles.size();
	}
	
	public String toString() {
		String nameToString = "{";
		for(int i=0; i<profiles.size(); i++) {
			nameToString = nameToString + profiles.get(i).toString();
		}
		nameToString+="}";
		return nameToString;
	}
	//------------------------------------------------------
	//	Getters and Setters
	//------------------------------------------------------
	public double[] getTimes() {
		return times;
	}

	public void setTime(double[] times) {
		this.times = times;
	}

	public void setNumberTests(int numberTests) {
		this.numberTests = numberTests;
	}

	public int getNumberTests() {
		return numberTests;
	}

	public ArrayList<HardwareProfile> getProfiles() {
		return profiles;
	}
	
}
