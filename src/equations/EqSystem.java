package equations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import terms.Term;

/**
 *
 * @author Aurelien
 */
public class EqSystem {
    
    private final int numberOfVars;
    private int numberOfTotalVars;
    
    private final List<Equation> equations;
    private final HashMap<Integer, Substitute> substitutes;
    
    private Stack<Substitute> results;
    private HashMap<Integer, BigInteger> solutions;
    
    public EqSystem(int numberOfEquations, int numberOfVars) {
        this.equations = new ArrayList<>(numberOfEquations);
        this.substitutes = new HashMap<>();
        
        this.results = new Stack<>();
        this.solutions = new HashMap<>();
        
        this.numberOfVars = numberOfVars;
        this.numberOfTotalVars = numberOfVars;
    }    
    
    /**
     * Solve the system using the procedure of the lecture.
     */
    public void solve() {
    	while (equations.size() >= 1) {    		
        	// If possible, eliminate an equation with one term.
    		boolean hasDirectlyEliminated = this.eliminateDirectly();
    		
    		// Else, introduce a new variable.
    		if (!hasDirectlyEliminated) {
    			this.eliminate();
    		}
    		
    		// System.out.println("--------------");
    		// System.out.println(this);
    		// System.out.println(this.equations.size());
    		// System.out.println(this.substitutes);
    		// System.out.println("--------------");
    	}
    	
    	this.buildSolutions();
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
        		if (equations.size() == 0) {
        			sub.fixRemainingVariables(solutions);
        		}
        		
        		this.replaceSubstituteInEquations(sub, equation);
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
    	
    	// Send the substitute to all equations
//    	for (Equation equation : equations) {
//    		equation.integrateSubstitute(sub, model);
//    	}
    	
    	// Each equation is independent from the others
    	equations.parallelStream().forEach((equation) -> {
    		equation.integrateSubstitute(sub, model);
    	});
    }
    
    private void replaceSubstituteInEquations(Substitute sub, Equation model) {
    	Iterator<Equation> itEq = equations.iterator();
    	Equation equation;
    	
    	while (itEq.hasNext()) {
    		equation = itEq.next();
    		
    		if (equation.integrateSubstitute(sub, model))
    			itEq.remove();
    	}
    }
    
    private Equation findFollowingEquation() {
    	BigInteger minNorm = equations.get(0).getTerms().first().getA().abs();
    	Equation model = null;
    	
    	for (Equation equation : equations) {
    		if (equation.getTerms().first().getA().abs().compareTo(minNorm.abs()) < 1) {
    			minNorm = equation.getTerms().first().getA();
    			model = equation;
    		}
    	}
    	
    	return model;
    }
    
    private void buildSolutions() {    	
    	while (!this.results.isEmpty()) {
    		Substitute sub = this.results.pop();
    		int var = sub.getWillReplace();
    		
    		BigInteger sol = BigInteger.valueOf(0);
    		sol = sol.subtract(sub.getConstant());
    		
    		Iterator<Term> itTerms = sub.getTerms().messyIterator();
    		
    		while (itTerms.hasNext()) {
    			Term t = itTerms.next();
    			// System.out.println(sub + " " + solutions);
    			sol = sol.add(t.getA().multiply(solutions.get(t.getI())));
    		}
    		
    		solutions.put(var, sol);
    	}
    	
    	// System.out.println("oui *************************");
    	// System.out.println(solutions);
    	this.printSolutions();
    }
    
    private void printSolutions() {
    	for (int i = 1; i <= this.numberOfVars; ++i) {
    		if (solutions.containsKey(i))
    			System.out.print(solutions.get(i) + " ");
    		else
    			System.out.print("0 ");
    	}
    }
    
    /**
     * Return `true` if the equation is satisfiable and prepared.
     * @param numbers
     * @return
     */
    public boolean addAndPrepareEquation(List<Integer> numbers) {
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


