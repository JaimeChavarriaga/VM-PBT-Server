package co.edu.uniandes.resources;

import java.io.File;

import co.edu.uniandes.conf.Configuration;
import co.edu.uniandes.model.HardwareProfile;
import co.edu.uniandes.util.Constants;
import co.edu.uniandes.util.Services;

/**
 * Class to represent a VirtualBox Virtual Machine
 * @author Carlos Eduardo GÛmez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 *
 */
public class VirtualBoxVM extends VirtualMachine {

	private Configuration configuration;	
	
	public VirtualBoxVM(String name, int nTests, Configuration conf ) {
		super(name, nTests);
		this.configuration = conf;
	}
	
	/**
	 * Turn on the Virtual Machine
	 */
	public void vmTurnOn() {
		Services.execute(configuration.getVirtualBoxHome() + "VBoxManage"
				+ Constants.SPACE + "startvm"
				+ Constants.SPACE + this.getName()
				);
	}

	/**
	 * Turn off the Virtual Machine
	 */
	public void vmTurnOff() {
		Services.execute(configuration.getVirtualBoxHome() + "VBoxManage"
				+ Constants.SPACE + "controlvm"
				+ Constants.SPACE + this.getName()
				+ Constants.SPACE + "acpipowerbutton"
				);
	}
	
	/**
	 * Modify the Virtual Machine with the Hardware Profile
	 */
	public void vmModifyHardwareProfile() {
		HardwareProfile toSet = this.getProfile();
		
		Services.execute(configuration.getVirtualBoxHome() + "VBoxManage"
				+ Constants.SPACE + "modifyvm"
				+ Constants.SPACE + this.getName()
				+ Constants.SPACE + "--cpus"
				+ Constants.SPACE + toSet.getCores()
				+ Constants.SPACE + "--memory"
				+ Constants.SPACE + (toSet.getRAM() * 1024)
				);

	}
	
	/* Depende de la version. En 4.3 execute en lugar de run; --image en lugar de --exe
	 * 
	 * Los parametros van después de -- como en el siguiente ejemplo:

        VirtualBox\VBoxManage" --nologo guestcontrol Debian8.4-01 run --exe "/bin/ls" 
            --username root --password carlos --wait-stdout -- /bin/ls -l

	 */
	/**
	 * Send command to execute in the virtual machine
	 */
    private void vmExecuteCommand(String vmname, String name, String password, String command ) {
        // Validar los parámetros asignados
    	Services.execute(configuration.getVirtualBoxHome() + "VBoxManage"
                + Constants.SPACE + "--nologo"
                + Constants.SPACE + " guestcontrol"
                + Constants.SPACE + this.getName()
                + Constants.SPACE + "run"
                + Constants.SPACE + "--exe" 
                + Constants.SPACE + command 
                + Constants.SPACE + "--username"
                + Constants.SPACE + name
                + Constants.SPACE + "--password"
                + Constants.SPACE + password
                );
    }

    /**
     * Copy file in the virtual machine
     * @param vmname Name of the virtual machine
     * @param name of the User
     * @param password of the User
     * @param filename name of the file
     * @param destinationFolder Folder where the file is gonna be copied
     */
    private void copyFileToVM(String vmname, String name, String password, String filename, String destinationFolder) {
        // Validar los parámetros asignados
    	Services.execute(configuration.getVirtualBoxHome() + "VBoxManage"
                + Constants.SPACE + "--nologo"
                + Constants.SPACE + "guestcontrol"
                + Constants.SPACE + this.getName()
                + Constants.SPACE + "copyto"
                + Constants.SPACE + filename 
                + Constants.SPACE + "--target-directory"
                + Constants.SPACE + destinationFolder 
                + Constants.SPACE + "--username"
                + Constants.SPACE + name
                + Constants.SPACE + "--password"
                + Constants.SPACE + password
                );
    }
    
    public void registerVirtualMachine() {
    	Services.execute(configuration.getVirtualBoxHome() + "VBoxmanage"
    			+ Constants.SPACE + "registervm"
    			+ Constants.SPACE + "'" 
    			+ configuration.getVirtualMachinesHome()
    			+ File.pathSeparator + this.getName()
    			+ File.pathSeparator + this.getName() + Constants.VBOX
    			+ "'"
    			);
    }
    
    public void unregisterVirtualMachine() {
    	Services.execute(configuration.getVirtualBoxHome() + "VBoxmanage"
    			+ Constants.SPACE + "unregistervm"
    			+ Constants.SPACE + "'" 
    			+ configuration.getVirtualMachinesHome()
    			+ File.pathSeparator + this.getName()
    			+ File.pathSeparator + this.getName() + Constants.VBOX
    			+ "'"
    			);
    }
}
