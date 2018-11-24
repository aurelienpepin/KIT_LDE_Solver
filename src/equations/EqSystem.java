package equations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

/**
 *
 * @author Aurelien
 */
public class EqSystem {
    
    private final int numberOfVars;
    private int numberOfTotalVars;
    
    private final List<Equation> equations;
    private final HashMap<Integer, Substitute> substitutes;

    
    public EqSystem(int numberOfEquations, int numberOfVars) {
        this.equations = new ArrayList<>(numberOfEquations);
        this.substitutes = new HashMap<>();
        
        this.numberOfVars = numberOfVars;
        this.numberOfTotalVars = numberOfVars;
    }    
    
    /**
     * Solve the system using the procedure of the lecture.
     */
    public void solve() {
    	int T = 0;
    	
    	// while (numberOfTotalVars - this.substitutes.size() > 2) {
    	while (equations.size() >= 1 && T < 35) {
        	// If possible, eliminate an equation with one term.
    		boolean hasDirectlyEliminated = this.eliminateDirectly();
    		
    		// Else, introduce a new variable.
    		if (!hasDirectlyEliminated) {
    			this.eliminate();
    		}
    		
    		// System.out.println("--------------");
    		System.out.println(this);
    		System.out.println(this.equations.size());
    		System.out.println("--------------");
    		T++;
    	}
    	
    	// System.out.println(this.substitutes);
    }
    
    private boolean eliminateDirectly() {
    	Iterator<Equation> itEq = equations.iterator();

    	while (itEq.hasNext()) {
    		Equation equation = itEq.next();
    		
    		if (equation.isDirectlySolvable()) {
    			Substitute sub = equation.solveDirectly();
    			this.substitutes.put(sub.getWillReplace(), sub);
    		
        		itEq.remove();
        		
        		this.replaceSubstituteInEquations(sub, equation);
        		System.out.println(">>> DIRECT");
        		return true;
    		}
    	}    	
    	
    	return false;
    }
    
    private void eliminate() {
    	Equation model = equations.get(0);
    	Substitute sub = model.generateSubstitute(++numberOfTotalVars);    	
    	this.substitutes.put(sub.getWillReplace(), sub);
    	
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


