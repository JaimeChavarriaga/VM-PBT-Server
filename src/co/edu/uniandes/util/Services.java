package co.edu.uniandes.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Services in the Server
 * @author Carlos Eduardo Gómez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class Services {

	/**
	 * Execute terminal command in Host
	 * @param command
	 */
	public static void execute(String command) {
		Runtime r = Runtime.getRuntime();
		Process p = null;
		try {
			p = r.exec(command);

			String line;

			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
		} catch (Exception e) {
			System.err.println("Error while executing " + command);
		}
	}
	
	/**
	 * Pause n seconds the execution.
	 * @param n seconds.
	 */
	public static void pause(long n) {
		try {
			Thread.sleep(n * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
