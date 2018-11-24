package tools;

import java.util.List;

public class GCD {
	
	/**
	 * Compute the GCD of two numbers.
	 * @param a
	 * @param b
	 * @return
	 */
	public static int gcd(int a, int b) {
	    while (b != 0) {
	        int t = a;
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
	public static int gcd(List<Integer> a) {
		return a.stream().reduce((b,c) -> gcd(b,c)).get();
	}
}
