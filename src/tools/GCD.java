package tools;

import java.math.BigInteger;
import java.util.List;

public class GCD {
	
	private static BigInteger zero = new BigInteger("0");
	
	/**
	 * Compute the GCD of two numbers.
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigInteger gcd(BigInteger a, BigInteger b) {
	    while (b.compareTo(zero) != 0) {
	        BigInteger t = a.abs();
	        a = b;
	        b = t.remainder(b);
	    }
	    
	    return a;
	}
	
	/**
	 * Compute the GCD of n numbers.
	 * @param a
	 * @return
	 */
	public static BigInteger gcd(List<BigInteger> a) {
		return a.stream().reduce((b,c) -> gcd(b,c)).get();
	}
}
