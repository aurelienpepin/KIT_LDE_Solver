package test;

import java.io.File;

import equations.EqSystem;
import tools.Parser;

public class TestParseAll {

	public static void main(String[] args) {
		Parser parser = new Parser();
		
		File folder = new File(args[0]);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				EqSystem system = parser.parse(listOfFiles[i].getPath());
				
				if (system == null)
					System.out.println("Error for: " + listOfFiles[i].getPath());
			}
		}
	}
}
