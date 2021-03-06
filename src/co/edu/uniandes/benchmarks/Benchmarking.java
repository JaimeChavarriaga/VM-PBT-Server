package co.edu.uniandes.benchmarks;

import co.edu.uniandes.util.Constants;

/**
 * @author Carlos Eduardo G�mez Montoya
 * @author Cristian David Sierra Barrera
 * @author Harold Enrique Castro Barrera
 */
public class Benchmarking {
	
	/**
	 * Execute the type of benchmarking in the test
	 * @param name of benchmark
	 * @param param of benchmark
	 */
	public static void executeBenchmarking(String name, int param) {
		String[] sequence = name.split(Constants.SEPARATOR_BENCHMARKING);
		if(sequence[0].equals("INTEGERS")) {
			if(sequence[1].equals("PRIMES")) {
				if(sequence[2].equals("FINDNPRIMES")) {
					primeCalculatorIntegers(param);
				}
			} else if(sequence[1].equals("BIGINTEGERS")) {
				
			}
		} else if(sequence[0].equals("DOUBLES")) {
			
		} else if(sequence[0].equals("STRINGS")) {
			
		}
	}
	
	//----------------------------------------------------------
	//	Integers Benchmarks
	//----------------------------------------------------------
	/**
	 * Calculator n primes.
	 * @param n number of primes to find 
	 * Algorithm used from http://beginnersbook.com/2014/01/java-program-to-display-first-n-or-first-100-prime-numbers/
	 */
	public static void primeCalculatorIntegers(int n) {
		int status = 1;
		int num = 3;

		for (int i = 2; i <= n;) {
			for (int j = 2; j <= Math.sqrt(num); j++) {
				if (num % j == 0) {
					status = 0;
					break;
				}
			}
			if (status != 0) {
				//System.out.println(i);
				i++;
			}
			status = 1;
			num++;
		}
	}

	//----------------------------------------------------------
	//	Floats Benchmarks
	//----------------------------------------------------------
	
	
	//----------------------------------------------------------
	//	Strings Benchmarks
	//----------------------------------------------------------
	
	
}
