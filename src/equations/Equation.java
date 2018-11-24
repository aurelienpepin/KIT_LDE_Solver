package equations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import terms.Term;
import terms.TermComparator;
import terms.Terms;
import tools.GCD;
import tools.ModPrime;

/**
 *
 * @author Aurelien
 */
public class Equation {

	private int b;
    private final Terms terms;
    private final List<Integer> coefs;
    
    private final int gcdA;
    private boolean sat;
    
    public Equation(List<Integer> numbers) {
    	this.coefs = new ArrayList<>();
    	this.terms = new Terms();
    	
    	this.b = numbers.get(numbers.size() - 2);
    	this.sat = false;
    	
    	// Extract coefficients are get their GCD
    	this.gcdA = this.groupCoefs(numbers);
    	
    	// Prepare if satisfiable
    	int numberOfTerms = numbers.get(0);
    	
    	// Normalization
    	if ((sat = this.isSatisfiable())) {
    		for (int i = 0; i < numberOfTerms - 1; ++i) {
    			this.terms.add(new Term(numbers.get(2 * i + 1) / gcdA, numbers.get(2 * i + 2)));
    		}
    		
    		for (int i = 0; i < coefs.size(); ++i) {
    			coefs.set(i, coefs.get(i) / gcdA);
    		}
    		
    		this.b = b / gcdA;
    	}
	}
    
    private int groupCoefs(List<Integer> numbers) {
    	int numberOfTerms = numbers.get(0);
    	
    	for (int i = 0; i < numberOfTerms - 1; ++i) {
    		this.coefs.add(numbers.get(2 * i + 1));
    	}
    	
    	// Compute GCD for all coefficients
    	return GCD.gcd(coefs);
    }
    
    public boolean isSatisfiable() {
    	return sat || b % this.gcdA == 0;
    }
    
    /**
     * An equation is directly solvable iff it has the form a*x = b.
     * @return
     */
    public boolean isDirectlySolvable() {
    	return Math.abs(this.terms.first().getA()) == 1;
    }
    
    /**
     * Solve directly the equation a*x = b with x = b/a.
     * @return
     */
    public Substitute solveDirectly() {
    	return new Substitute(this);
    }
    
    /**
     * Replace a variable with the known result.
     * @param result
     */
    public void replaceResult(Result result) {
    	Term term = this.terms.get(result.getI());
    	
    	if (term != null) {
	    	this.b = b - result.getRes() * term.getA();
	    	this.terms.remove(term);
    	}
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
    public void integrateSubstitute(Substitute sub, Equation model) {
    	Term replaced = this.terms.get(sub.getWillReplace());    	
    	if (replaced == null) {
    		return; // Nothing to replace in this equation
    	}
    	
    	// Change the `b`.
    	this.b = b + sub.getConstant() * replaced.getA();
    	coefs.clear();
    	coefs.add(b);
    	
    	// Remove the replaced coefficient
    	this.terms.remove(sub.getWillReplace());    	

    	// Update the coefficients
    	Iterator<Term> itTerms = sub.getTerms().messyIterator();
		List<Term> toRemove = new ArrayList<>();
		List<Term> toAdd = new ArrayList<>();
		
		Terms existings = new Terms();
		int newA;
		
		while (itTerms.hasNext()) {
			Term t = itTerms.next();
			
			// Does the `sub`-term exist in the equation?
			Term existing = this.terms.get(t.getI());
			
			if (existing == null) {
				newA = replaced.getA() * t.getA();
				if (newA != 0)
					toAdd.add(new Term(newA, t.getI()));
			} else {
				newA = replaced.getA() * t.getA() + existing.getA();
				toRemove.add(existing);
				existings.add(existing);
				
				if (newA != 0)
					toAdd.add(new Term(newA, t.getI()));
			}
		}
		
		for (Term term : toRemove) this.terms.remove(term);
		for (Term term : toAdd) this.terms.add(term);		
		
		// Get the coefficients
		for (Term term : this.terms)
			coefs.add(term.getA());
    	
    	// Simplify?
    	this.simplifyIfPossible(existings, model);
    }
    
    /**
     * Use the GCD to see if the equation is simplifiable.
     */
    private void simplifyIfPossible(Terms existings, Equation model) {
    	int gcd = GCD.gcd(coefs);

    	if (coefs.size() > 1 && Math.abs(gcd) != 1) {
    		this.b /= gcd;
    		Iterator<Term> itTerms = this.terms.messyIterator();
    		List<Term> toRemove = new ArrayList<>();
    		List<Term> toAdd = new ArrayList<>();
    		
    		while (itTerms.hasNext()) {
    			Term t = itTerms.next();	
    			
    			toRemove.add(t);    			
    			toAdd.add(new Term(t.getA() / gcd, t.getI()));
    			
    			if (model == this && existings.get(t.getI()) != null) {
    				System.out.println("ALORS " + existings.get(t.getI()).getA() + " EST DEVENU " + t.getA() / gcd);
    				
    				if (Math.abs(existings.get(t.getI()).getA()) <= Math.abs(t.getA() / gcd)) {
    					System.out.println("PROBLEME !!!!!!!!!!!!!!!!!!!!!!!");
    					System.exit(1);
    				}
    			}
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
    		t.flipA();
    	}
    	
    	this.b *= -1;
    }
    
    public Terms getTerms() {
		return terms;
	}
    

	public int getB() {
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
