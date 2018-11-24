package kit_lindiopheq_solver_java;

import equations.EqSystem;
import tools.Parser;

/**
 *
 * @author Aurelien
 */
public class KIT_LinDiophEq_Solver_Java {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Parser parser = new Parser();
        EqSystem system = parser.parse("C:\\Users\\Aurelien\\Documents\\KIT\\Cours\\Entscheidungsverfahren\\Programme\\KIT_LinDiophEq_Solver\\examples\\lin-eq-ex\\ex-5-5-4.leq");
        
        if (system == null) {
        	System.out.println("UNSAT");
        } else {
        	System.out.println("SAT");
        	
            System.out.println("AVANT:\n" + system);
            system.solve();
            System.out.println("APRES:\n" + system);
        }
    }
}
