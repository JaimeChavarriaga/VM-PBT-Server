package co.edu.uniandes.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import co.edu.uniandes.benchmarks.Benchmarking;
import co.edu.uniandes.conf.Configuration;
import co.edu.uniandes.model.HardwareProfile;
import co.edu.uniandes.model.Scenario;
import co.edu.uniandes.resources.VirtualMachine;
import co.edu.uniandes.util.Constants;
import co.edu.uniandes.util.LoggerUtil;
import co.edu.uniandes.util.Services;

/**
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class AdminServer {
	
	//-----------------------------------------------------------
	//	Variables
	//-----------------------------------------------------------
	private Configuration configuration;
	private ServerSocket welcomeSocket;
	private Socket connectionSocket;
	private ObjectOutputStream outToClient;
	private ObjectInputStream inFromClient;

	private FileOutputStream out;
	private File file;

	/**
	 * Scenarios of the Test
	 */
	private ArrayList<Scenario> scenarios;
	
	/**
	 * Hardware Profiles of VirtualMachine in the Tests
	 */
	private HashMap<String, HardwareProfile> profiles;
	
	/**
	 * Machines to execute tests
	 */
	private ArrayList<VirtualMachine> machines;
	
	/**
	 * List of priorities to execute
	 */
	private ArrayList<String> priorities;
	
	/**
	 * List of kind test on clients
	 */
	private ArrayList<String> testsClient;
	
	/**
	 * Name of Output File
	 */
	private String nameOutputFile;
	
	/**
	 * Name of Benchmark
	 */
	private String nameBenchmark;
	
	/**
	 * Paramater of Benchmark
	 */
	private int paramBenchmark;
	
	/**
	 * Prefix for Virtual Machines
	 */
	private String prefixNameMachines;
	
	/**
	 * Number of test by each scenario
	 */
	private int testsScenarios;
	
	/**
	 * Instance of Logger
	 */
	private Logger log;

	/**
	 * Constructor
	 */
	public AdminServer() {
		configuration = new Configuration (Constants.PROPERTIES_FILENAME);
		
		// logger setup
		try {
			log = LoggerUtil.getLoggerInfo(configuration.getLabelLogFile(), configuration.getPathLog(),
					configuration.getLogFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load configuration of Tests
	 */
	public void load(String name) {
		
		// Preparation
		profiles = new HashMap<String, HardwareProfile>();
		scenarios = new ArrayList<Scenario>();
		machines = new ArrayList<VirtualMachine>();
		priorities = new ArrayList<String>();
		testsClient = new ArrayList<String>();
		nameOutputFile = null;
		paramBenchmark = 0;
		prefixNameMachines = null;
		
		// Load everything necessaries for the tests.
		LoadTestSpecification.loadConfiguration(this, name);

	}
	
	/**
	 * Copy the client to Hypervisor and Clone based on the maximum scenario.
	 */
	public void installVirtualMachines() {
		// TODO Implement the installation		
		log.info("Installing "+machines.size()+" virtual machines.");
		
		// Create virtual machines.
		
	}
	
	/**
	 * Execute the tests
	 */
	public void run () {
		log.info("Total Test starts!");

		// Load
		for(int l=0; l<testsClient.size(); l++) {
			String type = testsClient.get(l);
			log.info("Executing Load: "+type);
			
			// Priorities
			for(int p=0; p<priorities.size(); p++) {
				String priority = priorities.get(p);
				
				log.info("Executing with priority: "+priority);
				// Scenarios
				for(int s=0; s<scenarios.size(); s++) {
					Scenario actualScenario = scenarios.get(s);
					setupMachinesWithScenario(actualScenario);
					executeTests(actualScenario, type, priority);
					saveInformation(actualScenario);
					Services.pause(5);
				}
				this.saveInformationByLoad(type, priority);
			}
		}
		
		log.info("Total Test ends!");		
	}
	
	/**
	 * Execute by test with all scenarios.
	 */
	public void runByTests () {
		log.info("Total Test starts!");

		for(int t=0; t<this.getTestsScenarios(); t++) {
			log.info("Begin Test #"+t);
			// Load
			for(int l=0; l<testsClient.size(); l++) {
				String type = testsClient.get(l);
				log.info("Executing Load: "+type);
				
				// Priorities
				for(int p=0; p<priorities.size(); p++) {
					String priority = priorities.get(p);
					
					log.info("Executing with priority: "+priority);
					// Scenarios
					for(int s=0; s<scenarios.size(); s++) {
						Scenario actualScenario = scenarios.get(s);
						setupMachinesWithScenario(actualScenario);
						executeOneTest(actualScenario, type, priority,t);
						Services.pause(5);
					}					
					this.saveInformationByTest(type, priority, t);
				}
			}
			log.info("End Test #"+t);
		}
		
		log.info("Total Test ends!");		
	}
	
	private void saveInformation(Scenario actualScenario) {
		String path = "."+File.separator+"data"+File.separator+nameOutputFile+"Scenarios"+Constants.CSV;
		
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileOutputStream(new File(path), true));
		
			printWriter.println();
			printWriter.println("SCENARIO,"+actualScenario.toString());
			
			double[] times = actualScenario.getTimes();
			for(int i=0; i<times.length; i++) {
				if(i==0) {
					printWriter.print(times[i]);	
				} else {
					printWriter.print(","+times[i]);
				}
			}
			printWriter.println();
			printWriter.println("Average,"+actualScenario.averageTime());
			printWriter.println();
			
			printWriter.close();	
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
		    if(printWriter != null){
		    	printWriter.close();
		    }
		} 
	}

	/**
	 * Save information by the test in execution
	 * @param name of output file
	 * @param priority to keep
	 * @param test in execution
	 */
	private void saveInformationByTest(String name, String priority, int test) {
		String path = "."+File.separator+"data"+File.separator+nameOutputFile+"_"+(test+1)+Constants.CSV;
		
		// Scenario 0
		double timeScenario0;
		log.info("Begin the process");
		long startTime = System.nanoTime();
		Benchmarking.executeBenchmarking(nameBenchmark, paramBenchmark);
		long endTime = System.nanoTime();			
		log.info("End the process");
		timeScenario0=(endTime-startTime)/Constants.MILLON;
		
		PrintWriter printWriter = null;
		try {
			File f = new File(path);
			printWriter = new PrintWriter(new FileOutputStream(f, true));
			
			if(!f.exists()) { 
				printWriter.println("Test:,"+test);
				printWriter.println();
			}
			printWriter.println("Load:,"+name);
			printWriter.println("Priority:,"+priority);
			
			//	Header
			printWriter.println("Scenarios, Time");
			
			//	Tests
			// Scenario 0
			printWriter.println("Scenario 0,"+timeScenario0);
			String row = "";
			for(int s=0; s<this.getScenarios().size(); s++) {
				Scenario toSave = this.getScenarios().get(s);
				row = "Scenario "+(s+1)+","+toSave.getTimeByTest(test);
				printWriter.println(row);
			}
			
			printWriter.close();	
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
		    if(printWriter != null){
		    	printWriter.close();
		    }
		} 
	}
	
	private void saveInformationByLoad(String name, String priority) {
		String path = "."+File.separator+"data"+File.separator+nameOutputFile+"_"+name+Constants.CSV;
		
		// Scenario 0
		double[] timesScenario0 = new double[this.getTestsScenarios()];
		for(int t=0; t<timesScenario0.length; t++) {
			log.info("Begin the process");
			long startTime = System.nanoTime();
			Benchmarking.executeBenchmarking(nameBenchmark, paramBenchmark);
			long endTime = System.nanoTime();			
			log.info("End the process");
			
			timesScenario0[t]=(endTime-startTime)/Constants.MILLON;
		}
		
		PrintWriter printWriter = null;
		try {
			File f = new File(path);
			printWriter = new PrintWriter(new FileOutputStream(f, true));
			
			if(f.exists()) { 
				printWriter.println("Test: "+name);
				printWriter.println();
			}
			printWriter.println("Priority: "+priority);
			//	Header
			String header = "Test\\Scenarios,Scenario 0";
			for(int s=0; s<this.getScenarios().size(); s++) {
				header+=",Scenario "+(s+1);
			}
			printWriter.println(header);
			
			//	Tests
			String row = "";
			for(int t=0; t<this.getTestsScenarios(); t++) {
				row = "Test "+(t+1)+","+timesScenario0[t];
				for(int s=0; s<this.getScenarios().size(); s++) {
					row+=","+this.getScenarios().get(s).getTimeByTest(t);
				}
				printWriter.println(row);
			}
			
			printWriter.close();	
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
		    if(printWriter != null){
		    	printWriter.close();
		    }
		} 
	}
	
	/**
	 * Modify the virtual Machines according to Scenario
	 * @param toTest
	 */
	public void setupMachinesWithScenario(Scenario toTest) {
		ArrayList<HardwareProfile> profilesScenario = toTest.getProfiles();
		for(int i=0; i<profilesScenario.size(); i++) {
			HardwareProfile toSet = profilesScenario.get(i);
			machines.get(i).setProfile(toSet);
		}
	}

	/**
	 * Execute one test for the params given.
	 * @param toTest Scenario
	 * @param type of load
	 * @param priority to execute
	 * @param number of test to execute
	 */
	public void executeOneTest(Scenario toTest, String type, String priority, int test) {
		log.info("SCENARIO "+toTest.toString());
		int maxMachines = toTest.getProfiles().size();
			
		long[] startSignUp = new long[maxMachines];
		long[] endSignUp = new long[maxMachines];
		
		// Turn on Machines
		for(int m=0; m<maxMachines; m++) {
			VirtualMachine machine = machines.get(m);
			machine.vmTurnOn();
			if(m==0) {
				Services.execute("wmic process where name=\"VBoxSVC.exe\" CALL setpriority "+priority);
			}
			startSignUp[m] = System.nanoTime();
		}
		
		// Threads for machines
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(configuration.getPort());
			for(int m=0; m<maxMachines; m++) {
				Socket socketClient = listener.accept();

				// Measure of SignUp
				endSignUp[m] = System.nanoTime();
				
				// Connection to communicate
				PrintWriter communicationOut = new PrintWriter(socketClient.getOutputStream(), true);

                // Receive time of Sign Up
                VirtualMachine machine = machines.get(m);
                machine.setTimeSignUpByTest(startSignUp[m], endSignUp[m], test);
                log.info("Client arrived: " + socketClient.getInetAddress()  + " Spin up: " + machine.getTimeByTest(test));
                
                // Send test configuration to Client
                communicationOut.println(type);
                log.info("Connected Machine "+(m+1));	
			}
                
            // Wait time for stabilization
            Services.pause(Constants.TIME_STABILIZATION);

            // Execute Benchmarking
            log.info("Begin the process");
            long startTime = System.nanoTime();
            Benchmarking.executeBenchmarking(nameBenchmark, paramBenchmark);
            long endTime = System.nanoTime();
            log.info("End the process");
            
			// Update Information
            toTest.setTimebyTest(startTime, endTime, test);
            log.info("Process Time: "+toTest.getTimeByTest(test));
			
		} catch (IOException e) {
			log.error("Error in Sockets ");
			e.printStackTrace();
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				log.error("Second Error in Socket");
			}
		}
		
		// Turn off Machines
		for(int m=0; m<maxMachines; m++) {
			VirtualMachine machine = machines.get(m);
			machine.vmTurnOff();
		}
		
		Services.pause(5);
		
	}

	
	/**
	 * Execute the scenario according the times indicated.
	 * @param toTest Scenario
	 */
	public void executeTests(Scenario toTest, String type, String priority) {
		log.info("SCENARIO "+toTest.toString());
		int numberTests = toTest.getNumberTests();
		int maxMachines = toTest.getProfiles().size();
		for(int test=0; test<numberTests; test++) {
			log.info("Test #"+(test+1));
			
			long[] startSignUp = new long[maxMachines];
			long[] endSignUp = new long[maxMachines];
			
			// Turn on Machines
			for(int m=0; m<maxMachines; m++) {
				VirtualMachine machine = machines.get(m);
				machine.vmTurnOn();
				if(m==0) {
					Services.execute("wmic process where name=\"VBoxSVC.exe\" CALL setpriority "+priority);
				}
				startSignUp[m] = System.nanoTime();
			}
			
			// Threads for machines
			ServerSocket listener = null;
			try {
				listener = new ServerSocket(configuration.getPort());
				for(int m=0; m<maxMachines; m++) {
					Socket socketClient = listener.accept();

					// Measure of SignUp
					endSignUp[m] = System.nanoTime();
					
					// Connection to communicate
					PrintWriter communicationOut = new PrintWriter(socketClient.getOutputStream(), true);

	                // Receive time of Sign Up
	                VirtualMachine machine = machines.get(m);
	                machine.setTimeSignUpByTest(startSignUp[m], endSignUp[m], test);
	                log.info("Client arrived: " + socketClient.getInetAddress()  + " Spin up: " + machine.getTimeByTest(test));
	                
	                // Send test configuration to Client
	                communicationOut.println(type);
	                log.info("Connected Machine "+(m+1));	
				}
	                
                // Wait time for stabilization
                Services.pause(Constants.TIME_STABILIZATION);

                // Execute Benchmarking
                log.info("Begin the process");
                long startTime = System.nanoTime();
                Benchmarking.executeBenchmarking(nameBenchmark, paramBenchmark);
                long endTime = System.nanoTime();
                log.info("End the process");
                
    			// Update Information
                toTest.setTimebyTest(startTime, endTime, test);
                log.info("Procces Time: "+toTest.getTimeByTest(test));
				
			} catch (IOException e) {
				log.error("Error in Sockets ");
				e.printStackTrace();
			} finally {
				try {
					listener.close();
				} catch (IOException e) {
					log.error("Second Error in Socket");
				}
			}
			
			// Turn off Machines
			for(int m=0; m<maxMachines; m++) {
				VirtualMachine machine = machines.get(m);
				machine.vmTurnOff();
			}
			
			Services.pause(5);
		}
	}
	
	/**
	 * Delete the clients from Hypervisor.
	 */
	public void uninstallVirtualMachines() {
		// TODO Implement to uninstall
	}

	/**
	 * Close connections between the clients and the server.
	 * servidor. Primero se cierran los flujos y luego se cierra el socket.
	 */
	public void closeConnection() {
		try {
			if (outToClient != null)
				outToClient.close();
			if (inFromClient != null)
				inFromClient.close();
			if (connectionSocket != null)
				connectionSocket.close();
			if (welcomeSocket != null)
				welcomeSocket.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Create the flows and connection between the clients and the server.
	 * @throws IOException
	 */
	private void connect() throws IOException {
		inFromClient = new ObjectInputStream(connectionSocket.getInputStream());

		outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
	}

	/**
	 * Main method, execute the server functions
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		AdminServer a = new AdminServer();

		// Searching files to execute
		ArrayList<String> testFiles = new ArrayList<String>();
		
		String pathDirectory = "."+File.separator+"data"+File.separator;
		File folder = new File(pathDirectory);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if(listOfFiles[i].getName().endsWith(Constants.TYPE_FILES)) {
					testFiles.add(listOfFiles[i].getName());
				}
			}
		}
		
		// Execute each test
		for(String file:testFiles) {
			a.load(file);
			a.installVirtualMachines();
			a.run();
			//a.runByTests();
			a.uninstallVirtualMachines();
		}
	}
	
	//------------------------------------------------------------
	//	Getters and Setters
	//------------------------------------------------------------
	public ArrayList<Scenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(ArrayList<Scenario> scenarios) {
		this.scenarios = scenarios;
	}

	public HashMap<String, HardwareProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(HashMap<String, HardwareProfile> profiles) {
		this.profiles = profiles;
	}

	public ArrayList<VirtualMachine> getMachines() {
		return machines;
	}

	public void setMachines(ArrayList<VirtualMachine> machines) {
		this.machines = machines;
	}

	public ArrayList<String> getPriorities() {
		return priorities;
	}

	public void setPriorities(ArrayList<String> priorities) {
		this.priorities = priorities;
	}

	public ArrayList<String> getTestsClient() {
		return testsClient;
	}

	public void setTestsClient(ArrayList<String> testsClient) {
		this.testsClient = testsClient;
	}

	public String getNameOutputFile() {
		return nameOutputFile;
	}

	public void setNameOutputFile(String nameOutputFile) {
		this.nameOutputFile = nameOutputFile;
	}

	public int getNumberOfPrimes() {
		return paramBenchmark;
	}

	public void setNumberOfPrimes(int numberOfPrimes) {
		this.paramBenchmark = numberOfPrimes;
	}

	public String getPrefixNameMachines() {
		return prefixNameMachines;
	}

	public void setPrefixNameMachines(String prefixNameMachines) {
		this.prefixNameMachines = prefixNameMachines;
	}

	public int getTestsScenarios() {
		return testsScenarios;
	}

	public void setTestsScenarios(int testsScenarios) {
		this.testsScenarios = testsScenarios;
	}
	
	public int getParamBenchmark() {
		return paramBenchmark;
	}

	public void setParamBenchmark(int paramBenchmark) {
		this.paramBenchmark = paramBenchmark;
	}

	public String getNameBenchmark() {
		return nameBenchmark;
	}

	public void setNameBenchmark(String nameBenchmark) {
		this.nameBenchmark = nameBenchmark;
	}
}