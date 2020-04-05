package tools;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class ModPrime {
	
	private static BigDecimal oneHalf = new BigDecimal("0.5");
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigInteger calc(BigInteger a, BigInteger b) {
		BigDecimal divResult = new BigDecimal(a).divide(new BigDecimal(b), 2, RoundingMode.HALF_UP);
		BigInteger floorResult = divResult.add(oneHalf).toBigInteger();
		
		return a.subtract(b.multiply(floorResult));
		
	    // return a - b * ((long) Math.floor(a / (float) b + 1 / 2.0));
	}
}
