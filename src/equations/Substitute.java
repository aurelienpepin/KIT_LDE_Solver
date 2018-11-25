package equations;

import java.util.Iterator;

import terms.Term;
import terms.Terms;
import tools.ModPrime;

public class Substitute {

	private final long constant;
	private final Terms terms;
	
	private final long newVar;
	private final long willReplace;
	private final long m;
	
	/**
	 * Create a substitute which introduces a new variable.
	 * @param equation
	 * @param newVar
	 */
	public Substitute(Equation equation, long newVar) {
		System.out.println("EQCHOISIE: " + equation);
		
		Term first = equation.getTerms().first();
		long a = first.getA();
		
		System.out.println("INFOS1: first=" + first);
		
		if (a < 0)
			equation.flipSigns();

		first = equation.getTerms().first();
		a = first.getA();
		this.m = a + 1;
		
		// Generate constant of the substitute
		this.constant = ModPrime.calc(equation.getB(), m);
		
		// Generate new coefficients
		this.terms = new Terms();
		Iterator<Term> itTerms = equation.getTerms().messyIterator();

		long newVal;
		while (itTerms.hasNext()) {
			Term t = itTerms.next();	
			
			if (t.getI() != first.getI() && (newVal = ModPrime.calc(t.getA(), m)) != 0)
				this.terms.add(new Term(newVal, t.getI()));
		}
		
		// Generate new variable
		this.terms.add(new Term(-1 * m, newVar));
		
		this.willReplace = first.getI();
		this.newVar = newVar;
		
		System.out.println("SUB(1): " + this);
	}
	
	/**
	 * Create a subtitute which doesn't introduce a new variable.
	 * When the norm is 1.
	 * @return
	 */
	public Substitute(Equation equation) {
		System.out.println("EQCHOISIE: " + equation);
		
		Term first = equation.getTerms().first();
		long a = first.getA();
		
		System.out.println("INFOS1: first=" + first);
		
		if (a < 0)
			equation.flipSigns();

		first = equation.getTerms().first();
		a = first.getA();
		this.m = a + 1;
		
		// Generate constant of the substitute
		this.constant = -1 * equation.getB();
		
		// Generate new coefficients
		this.terms = new Terms();
		Iterator<Term> itTerms = equation.getTerms().messyIterator();

		long newVal;
		while (itTerms.hasNext()) {
			Term t = itTerms.next();	
			
			if (t.getI() != first.getI()) {
				newVal = -1 * t.getA();
				this.terms.add(new Term(newVal, t.getI()));
			}
		}
		
		this.willReplace = first.getI();
		this.newVar = -1;
		
		System.out.println("SUB(2): " + this);
	}
	
	
	public long getWillReplace() {
		return willReplace;
	}

	public long getConstant() {
		return constant;
	}
	
	public Term get(long i) {
		return terms.get(i);
	}

	public long getNewVar() {
		return newVar;
	}
	
	public long getM() {
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
