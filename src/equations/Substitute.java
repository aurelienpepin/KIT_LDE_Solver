package equations;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

import terms.Term;
import terms.Terms;
import tools.ModPrime;

public class Substitute {

	private final BigInteger constant;
	private final Terms terms;
	
	private final int newVar;
	private final int willReplace;
	private final BigInteger m;
	
	private static BigInteger zero = new BigInteger("0");
	
	/**
	 * Create a substitute which introduces a new variable.
	 * @param equation
	 * @param newVar
	 */
	public Substitute(Equation equation, int newVar) {
		// System.out.println("EQCHOISIE: " + equation);
		
		Term first = equation.getTerms().first();
		BigInteger a = first.getA();
		
		// System.out.println("INFOS1: first=" + first);
		
		if (a.signum() < 0)
			equation.flipSigns();

		first = equation.getTerms().first();
		a = first.getA();
		this.m = a.add(BigInteger.ONE);
		
		// Generate constant of the substitute
		this.constant = ModPrime.calc(equation.getB(), m);
		
		// Generate new coefficients
		this.terms = new Terms();
		Iterator<Term> itTerms = equation.getTerms().messyIterator();

		BigInteger newVal;
		while (itTerms.hasNext()) {
			Term t = itTerms.next();	
			
			if (t.getI() != first.getI() && (newVal = ModPrime.calc(t.getA(), m)).compareTo(zero) != 0)
				this.terms.add(new Term(newVal, t.getI()));
		}
		
		// Generate new variable
		this.terms.add(new Term(m.negate(), newVar));
		
		this.willReplace = first.getI();
		this.newVar = newVar;
		
		// System.out.println("SUB(1): " + this);
	}
	
	/**
	 * Create a subtitute which doesn't introduce a new variable.
	 * When the norm is 1.
	 * @return
	 */
	public Substitute(Equation equation) {
		// System.out.println("EQCHOISIE: " + equation);
		
		Term first = equation.getTerms().first();
		BigInteger a = first.getA();
		
		// System.out.println("INFOS1: first=" + first);
		
		if (a.signum() < 0)
			equation.flipSigns();

		first = equation.getTerms().first();
		a = first.getA();
		this.m = a.add(BigInteger.ONE);
		
		// Generate constant of the substitute
		this.constant = equation.getB().negate();
		
		// Generate new coefficients
		this.terms = new Terms();
		Iterator<Term> itTerms = equation.getTerms().messyIterator();

		BigInteger newVal;
		while (itTerms.hasNext()) {
			Term t = itTerms.next();	
			
			if (t.getI() != first.getI()) {
				newVal = t.getA().negate();
				this.terms.add(new Term(newVal, t.getI()));
			}
		}
		
		this.willReplace = first.getI();
		this.newVar = -1;
		
		// System.out.println("SUB(2): " + this);
	}
	
	public void fixRemainingVariables(HashMap<Integer, BigInteger> solutions) {
		for (Term term : this.terms) {
			solutions.put(term.getI(), BigInteger.ZERO);
		}
	}
	
	public int getWillReplace() {
		return willReplace;
	}

	public BigInteger getConstant() {
		return constant;
	}
	
	public Term get(int i) {
		return terms.get(i);
	}

	public int getNewVar() {
		return newVar;
	}
	
	public BigInteger getM() {
		return m;
	}

	public Terms getTerms() {
		return terms;
	}

	@Override
    public String toString() {
    	String eqRepr = "";
    	
    	for (Term term : terms) {
    		eqRepr += " + " + term.toString();	// Just ignore first plus
    	}
    	
    	eqRepr += " - " + this.constant;
    	return eqRepr;
    }	
}
