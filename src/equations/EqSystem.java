package equations;

import static java.lang.Math.toIntExact;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import terms.Term;

import java.util.Iterator;

/**
 *
 * @author Aurelien
 */
public class EqSystem {
    
    private final long numberOfVars;
    private long numberOfTotalVars;
    
    private final List<Equation> equations;
    private final HashMap<Long, Substitute> substitutes;
    private Stack<Substitute> results;
    
    private int compteur = 0;
    
    public EqSystem(long numberOfEquations, long numberOfVars) {
        this.equations = new ArrayList<>(toIntExact(numberOfEquations));
        this.substitutes = new HashMap<>();
        this.results = new Stack<>();
        
        this.numberOfVars = numberOfVars;
        this.numberOfTotalVars = numberOfVars;
    }    
    
    /**
     * Solve the system using the procedure of the lecture.
     */
    public void solve() {
    	int T = 0;
    	
    	// while (numberOfTotalVars - this.substitutes.size() > 2) {
    	while (equations.size() >= 1/* && T < 200000000 */) {
    		try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
        	// If possible, eliminate an equation with one term.
    		boolean hasDirectlyEliminated = this.eliminateDirectly();
    		
    		// Else, introduce a new variable.
    		if (!hasDirectlyEliminated) {
    			this.eliminate();
    		}
    		
    		System.out.println("--------------");
    		System.out.println(this);
    		System.out.println(this.equations.size());
    		System.out.println(this.substitutes);
    		System.out.println("--------------");
    		T++;
    	}
    	
    	this.buildSolutions();
    	// System.out.println(this.substitutes);
    }
    
    private boolean eliminateDirectly() {
    	Iterator<Equation> itEq = equations.iterator();

    	while (itEq.hasNext()) {
    		Equation equation = itEq.next();
    		
    		if (equation.isDirectlySolvable()) {
    			Substitute sub = equation.solveDirectly();
    			this.substitutes.put(sub.getWillReplace(), sub);
    			this.results.push(sub);
    			
        		itEq.remove();
        		
        		this.replaceSubstituteInEquations(sub, equation);
        		System.out.println(">>> DIRECT");
        		return true;
    		}
    	}    	
    	
    	return false;
    }
    
    private void eliminate() {
    	// Equation model = equations.get(0);
    	Equation model = this.findFollowingEquation();
    	
    	Substitute sub = model.generateSubstitute(++numberOfTotalVars);    	
    	this.substitutes.put(sub.getWillReplace(), sub);
    	this.results.push(sub);
    	
    	// System.out.println(this.substitutes);
    	System.out.println(">>> INDIRECT");
    	
    	// Send the substitute to all equations
    	for (Equation equation : equations) {
    		equation.integrateSubstitute(sub, model);
    	}
    }
    
    private void replaceSubstituteInEquations(Substitute sub, Equation model) {
    	for (Equation equation : equations) {
    		equation.integrateSubstitute(sub, model);
    	}
    }
    
    private Equation findFollowingEquation() {
    	long minNorm = Long.MAX_VALUE;
    	Equation model = null;
    	
    	for (Equation equation : equations) {
    		if (Math.abs(equation.getTerms().first().getA()) < Math.abs(minNorm)) {
    			minNorm = equation.getTerms().first().getA();
    			model = equation;
    		}
    	}
    	
    	return model;
    }
    
    private void buildSolutions() {
    	HashMap<Long, Long> solutions = new HashMap<>();
    	
    	while (!this.results.isEmpty()) {
    		Substitute sub = this.results.pop();
    		long var = sub.getWillReplace();
    		
    		long sol = 0;
    		sol -= sub.getConstant();
    		
    		Iterator<Term> itTerms = sub.getTerms().messyIterator();
    		
    		while (itTerms.hasNext()) {
    			Term t = itTerms.next();
    			// System.out.println(sub + " " + solutions);
    			sol += t.getA() * solutions.get(t.getI());
    		}
    		
    		solutions.put(var, sol);
    	}
    	
    	System.out.println("oui *************************");
    	System.out.println(solutions);
    }
    
    /**
     * Return `true` if the equation is satisfiable and prepared.
     * @param numbers
     * @return
     */
    public boolean addAndPrepareEquation(List<Long> numbers) {
        Equation equation = new Equation(numbers);
        
        if (equation.isSatisfiable()) {
        	this.equations.add(equation);
        	return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
    	String sysRepr = "";
    	
    	for (Equation equation : equations) {
    		sysRepr += equation.toString() + "\n";
    	}
    	
    	return sysRepr;
    }
}


