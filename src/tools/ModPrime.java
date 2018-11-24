package tools;

public class ModPrime {
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int calc(int a, int b) {
	    return a - b * ((int) Math.floor(a / (float) b + 1 / 2.0));
	}
}
