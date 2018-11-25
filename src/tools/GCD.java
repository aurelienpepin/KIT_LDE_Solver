package tools;

import java.util.List;

public class GCD {
	
	/**
	 * Compute the GCD of two numbers.
	 * @param a
	 * @param b
	 * @return
	 */
	public static long gcd(long a, long b) {
	    while (b != 0) {
	        long t = a;
	        a = b;
	        b = t % b;
	    }
	    return a;
	}
	
	/**
	 * Compute the GCD of n numbers.
	 * @param a
	 * @return
	 */
	public static long gcd(List<Long> a) {
		return a.stream().reduce((b,c) -> gcd(b,c)).get();
	}
}
