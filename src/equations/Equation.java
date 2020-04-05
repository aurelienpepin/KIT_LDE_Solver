package equations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import terms.Term;
import terms.Terms;
import tools.GCD;

/**
 *
 * @author Aurelien
 */
public class Equation {

	private BigInteger b;
    private final Terms terms;
    private final List<BigInteger> coefs;
    
    private final BigInteger gcdA;
    private boolean sat;
    
    private static BigInteger zero = new BigInteger("0");
    private static BigInteger one = new BigInteger("1");
    
    public Equation(List<Integer> numbers) {
    	this.coefs = new ArrayList<>();
    	this.terms = new Terms();
    	
    	this.b = BigInteger.valueOf(numbers.get(numbers.size() - 2));
    	this.sat = false;
    	
    	// Extract coefficients are get their GCD
    	this.gcdA = this.groupCoefs(numbers);
    	
    	// Prepare if satisfiable
    	long numberOfTerms = numbers.get(0);
    	
    	// Normalization
    	if ((sat = this.isSatisfiable())) {
    		for (int i = 0; i < numberOfTerms - 1; ++i) {
    			this.terms.add(new Term(BigInteger.valueOf(numbers.get(2 * i + 1)).divide(gcdA), numbers.get(2 * i + 2)));
    		}
    		
    		for (int i = 0; i < coefs.size(); ++i) {
    			coefs.set(i, coefs.get(i).divide(gcdA));
    		}
    		
    		this.b = b.divide(gcdA);
    	}
	}
    
    private BigInteger groupCoefs(List<Integer> numbers) {
    	long numberOfTerms = numbers.get(0);
    	
    	for (int i = 0; i < numberOfTerms - 1; ++i) {
    		this.coefs.add(BigInteger.valueOf(numbers.get(2 * i + 1)));
    	}
    	
    	// Compute GCD for all coefficients
    	return GCD.gcd(coefs);
    }
    
    public boolean isSatisfiable() {
    	return sat || b.remainder(this.gcdA).compareTo(zero) == 0;
    }
    
    /**
     * An equation is directly solvable iff it has the form a*x = b.
     * @return
     */
    public boolean isDirectlySolvable() {
    	return this.terms.first().getA().abs().compareTo(one) == 0;
    }
    
    /**
     * Solve directly the equation a*x = b with x = b/a.
     * @return
     */
    public Substitute solveDirectly() {
    	return new Substitute(this);
    }
    
    /**
     * Get the substitute (equation from the smallest a_k).
     * @return
     */
    public Substitute generateSubstitute(int nextVar) {
    	return new Substitute(this, nextVar);
    }
    
    /**
     * Integrate the computed substitute into the equation.
     * @param sub
     */
    public boolean integrateSubstitute(Substitute sub, Equation model) {
    	Term replaced = this.terms.get(sub.getWillReplace());    	
    	if (replaced == null) {
    		return false; // Nothing to replace in this equation
    	}
    	
    	BigInteger prevA = replaced.getA();
    	
    	// Change the `b`.
    	this.b = b.add(sub.getConstant().multiply(prevA));
    	coefs.clear();
    	coefs.add(b);
    	
    	// Remove the replaced coefficient
    	this.terms.remove(sub.getWillReplace());    	

    	// Update the coefficients
		Set<Term> toRemove = Collections.newSetFromMap(new ConcurrentHashMap<>()); // new ArrayList<>();
		Set<Term> toAdd = Collections.newSetFromMap(new ConcurrentHashMap<>()); // new ArrayList<>();
    	
//    	Set<Term> toRemove = new HashSet<>();
//    	Set<Term> toAdd = new HashSet<>();
//		
//		BigInteger newA;
//		Term existing;
//
//		for (Term t : sub.getTerms()) {
//			
//			// Does the `sub`-term exist in the equation?
//			existing = this.terms.get(t.getI());
//			
//			if (existing == null) {
//				newA = prevA.multiply(t.getA());
//				
//				if (newA.compareTo(zero) != 0)
//					toAdd.add(new Term(newA, t.getI()));
//			} else {
//				newA = prevA.multiply(t.getA()).add(existing.getA());
//				toRemove.add(existing);
//				
//				if (newA.compareTo(zero) != 0)
//					toAdd.add(new Term(newA, t.getI()));
//			}
//		}
		
		sub.getTerms().parallelStream().forEach((t) -> {
			BigInteger newA;
			Term existing;
			
			// Does the `sub`-term exist in the equation?
			existing = this.terms.get(t.getI());
			
			if (existing == null) {
				newA = prevA.multiply(t.getA());
				
				if (newA.compareTo(zero) != 0)
					toAdd.add(new Term(newA, t.getI()));
			} else {
				newA = prevA.multiply(t.getA()).add(existing.getA());
				toRemove.add(existing);
				
				if (newA.compareTo(zero) != 0)
					toAdd.add(new Term(newA, t.getI()));
			}
		});
		
		for (Term term : toRemove) this.terms.remove(term);
		for (Term term : toAdd) this.terms.add(term);
    	
    	// Simplify?
        if (this == model) {
        	for (Term term : this.terms)
    			coefs.add(term.getA());
        	
            this.simplifyIfPossible(model);
        }
        
        return this.terms.size() == 0;
    }
    
    /**
     * Use the GCD to see if the equation is simplifiable.
     */
    private void simplifyIfPossible(Equation model) {
    	BigInteger gcd = GCD.gcd(coefs);

    	if (coefs.size() > 1 && gcd.abs().compareTo(one) == 1) {
    		gcd = gcd.abs();
    		this.b = this.b.divide(gcd);
    		
    		Iterator<Term> itTerms = this.terms.messyIterator();
    		List<Term> toRemove = new ArrayList<>();
    		List<Term> toAdd = new ArrayList<>();
    		
    		while (itTerms.hasNext()) {
    			Term t = itTerms.next();	
    			
    			toRemove.add(t);    			
    			toAdd.add(new Term(t.getA().divide(gcd), t.getI()));
    		}
    		
        	for (Term term : toRemove)
        		this.terms.remove(term);
        	
        	for (Term term : toAdd)
        		this.terms.add(term);
    	}
    }
    
    /**
     * Flip all the signs of the equation (multiply * -1)
     */
    public void flipSigns() {
    	Iterator<Term> itTerms = this.terms.messyIterator();
    	
    	while (itTerms.hasNext()) {
    		Term t = itTerms.next();
    		t.negateA();
    	}
    	
    	this.b = this.b.negate();
    }
    
    public Terms getTerms() {
		return terms;
	}
    

	public BigInteger getB() {
		return b;
	}


	@Override
    public String toString() {
    	String eqRepr = "";
    	
    	for (Term term : terms) {
    		eqRepr += " + " + term.toString();	// Just ignore first plus
    	}
    	
    	eqRepr += " = " + b;
    	return eqRepr;
    }
}
