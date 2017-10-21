package co.edu.uniandes.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import co.edu.uniandes.conf.Configuration;
import co.edu.uniandes.model.HardwareProfile;
import co.edu.uniandes.model.Scenario;
import co.edu.uniandes.resources.VirtualBoxVM;
import co.edu.uniandes.resources.VirtualMachine;
import co.edu.uniandes.util.Constants;

/**
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class LoadTestSpecification {

	//-------------------------------------------------------------------------
	//	Constants
	//-------------------------------------------------------------------------

	public static final String TAG_PROFILES = "profiles";
	public static final String TAG_NAME = "name";
	public static final String TAG_RAM = "RAM";
	public static final String TAG_CORES = "cores";
	
	public static final String TAG_SCENARIOS = "scenarios";
	public static final String TAG_NAME_PROFILE = "nameProfile";
	public static final String TAG_NUMBER_TEST = "numberOfTests";
	public static final String TAG_OUTPUT_FILE = "OutputFilename";
	
	public static final String TAG_PRIORITIES = "priorities";
	
	public static final String TAG_CLIENT_TESTS = "clientTests";
	
	public static final String TAG_BENCHMARK = "benchmark";
	
	public static final String TAG_MACHINE_NAME_PREFIX = "machineNamePrefix";
	
	//-------------------------------------------------------------------------
	//	Methods
	//-------------------------------------------------------------------------

	/**
	 * Method to load information about Execution Tests
	 * @param scenariosListToLoad List of Scenarios
	 * @param hardwareProfilesListToLoad List of Hardware Profiles
	 * @return filename output of tests.
	 */
	public static void loadConfiguration(AdminServer admin, String nameFile) {
		StringBuilder dataFile = new StringBuilder();

		String path = "."+File.separator+"data"+File.separator+nameFile;
		
		// Reading the Information
        try
        {
        	FileReader in = new FileReader(path);
        	BufferedReader br = new BufferedReader (in);
        	
        	String cadena=br.readLine();
            while(cadena!=null && !cadena.equals("")){
            	if(!cadena.startsWith("//")){
            		dataFile.append(cadena);
            	}
            	cadena = br.readLine();
            }
            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        
        // Parsing JSON Object
        // First, Hardware Profiles
        JSONObject dataTest = new JSONObject(dataFile.toString());
        JSONArray dataProfiles = dataTest.getJSONArray(TAG_PROFILES);
        for(int i=0; i<dataProfiles.length(); i++) {
        	JSONObject dataProfile = dataProfiles.getJSONObject(i);
        	String name = dataProfile.getString(TAG_NAME);
        	int RAM = dataProfile.getInt(TAG_RAM);
        	int cores = dataProfile.getInt(TAG_CORES);
        	HardwareProfile temp = new HardwareProfile(name, RAM, cores);
        	admin.getProfiles().put(name, temp);
        }
        
        // Second, number of times to execute the scenarios.
        int numberTests = dataTest.getInt(TAG_NUMBER_TEST);
        
        // Thid, Load scenarios, using the information about Hardware Profiles
		int maxVMs = 0;
        JSONArray dataScenarios = dataTest.getJSONArray(TAG_SCENARIOS);
        for(int i=0; i< dataScenarios.length(); i++) {
        	JSONArray dataScenario = dataScenarios.getJSONArray(i);
        	ArrayList<HardwareProfile> nProfiles = new ArrayList<HardwareProfile>();
        	
        	// Calculate the maximum virtual machines that the system needs
        	int sizeScenario = dataScenario.length();
        	if(sizeScenario>maxVMs) {
        		maxVMs = sizeScenario;
        	}
        	
        	// Continue with the load of scenario
        	for(int j=0; j<dataScenario.length(); j++){
        		JSONObject itemScenario = dataScenario.getJSONObject(j);
        		String keyName = itemScenario.getString(TAG_NAME_PROFILE);
        		nProfiles.add(admin.getProfiles().get(keyName));
        	}
        	Scenario temp = new Scenario(nProfiles, numberTests);
        	admin.getScenarios().add(temp);
        }
        
        // Four, output file.
        admin.setNameOutputFile(dataTest.getString(TAG_OUTPUT_FILE));

        // Five, Load the list of priorities.
        JSONArray dataPriorities = dataTest.getJSONArray(TAG_PRIORITIES);
        for(int i=0; i<dataPriorities.length(); i++) {
        	String temp = dataPriorities.getString(i);
        	admin.getPriorities().add(temp.toLowerCase());
        }
        
        // Six, Load the test of Client
        JSONArray dataTestsClient = dataTest.getJSONArray(TAG_CLIENT_TESTS);
        for(int i=0; i<dataTestsClient.length(); i++) {
        	String temp = dataTestsClient.getString(i);
        	admin.getTestsClient().add(temp.toUpperCase());
        }
        
        // Seven, Load the number of Primes to find in the Process
        JSONArray dataBenchmark = dataTest.getJSONArray(TAG_BENCHMARK);
        admin.setNameBenchmark(dataBenchmark.getString(0).toUpperCase());
        admin.setParamBenchmark(dataBenchmark.getInt(1));
        
        // Eight, Load the prefix name for machines
        admin.setPrefixNameMachines(dataTest.getString(TAG_MACHINE_NAME_PREFIX));
        
        // Nine, Number of tests by each scenario
        admin.setTestsScenarios(numberTests);
        
        // Ten. Load properties
        Configuration configuration = new Configuration(Constants.PROPERTIES_FILENAME); 
        
        // Create the virtual machines using the information load
        ArrayList<VirtualMachine> tempMachines = new ArrayList<VirtualMachine>();
        for(int i=0; i<maxVMs; i++) {
			VirtualBoxVM newVM = new VirtualBoxVM(admin.getPrefixNameMachines()+(i+1), numberTests, configuration);
			tempMachines.add(newVM);
		}
        admin.setMachines(tempMachines);
	}
}
