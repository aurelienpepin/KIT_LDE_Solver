package tools;

public class ModPrime {
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long calc(long a, long b) {
	    return a - b * ((long) Math.floor(a / (float) b + 1 / 2.0));
	}
}
