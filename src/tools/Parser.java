package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import equations.EqSystem;


/**
 *
 * @author Aurelien
 */
public class Parser {
    
    public EqSystem parse(String filePath) {
        EqSystem system = null;
        BufferedReader bf = null;
        
        try {
            bf = new BufferedReader(new FileReader(filePath));        
            system = this.getContent(bf);
            
        } catch (IOException ex) {
            System.err.println("Impossible to read the .leq file");
        } finally {
            try {
                if (bf != null)
                    bf.close();
            } catch (IOException ex) {
                System.err.println("Impossible to close the reader");
            }
        }
        
        return system;
    }
    
    private EqSystem getContent(BufferedReader bf) throws IOException {
        EqSystem system;
        String line;
        
        // Read size of system
        List<Integer> sizes = this.extractIntegers(bf.readLine());
        system = this.setSizeOfSystem(sizes);
        
        // Add the equations      
        while ((line = bf.readLine()) != null) {
            List<Integer> numbers = extractIntegers(line);
            if (!system.addAndPrepareEquation(numbers)) {
            	return null;
            }
        }
        
        return system;
    }
    
    private EqSystem setSizeOfSystem(List<Integer> sizes) {
        if (sizes.size() != 2) {
            throw new RuntimeException("Bad first line.");
        }
            
        return new EqSystem(sizes.get(0), sizes.get(1));
    }
    
    private List<Integer> extractIntegers(String string) {
        List<Integer> numbers = new ArrayList<>();
        String[] strings = string.split("\\s");
        
        for (String numberToConv : strings) {
            numbers.add(Integer.parseInt(numberToConv));
        }
        
        return numbers;
    }
}
