package kit_lde_solver;

import equations.EqSystem;
import tools.Parser;

/**
 *
 * @author Aurelien
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Parser parser = new Parser();
        
        if (args.length == 0) {
        	System.out.println("Please give a .leq file as an input!");
        	System.exit(1);
        	
        	return;
        }
        
        String fileToParse = args[0];
        EqSystem system = parser.parse(fileToParse);
        
        if (system == null) {
        	System.out.println("UNSAT");
        } else {
        	System.out.println("SAT");
            system.solve();
        }
    }
}
