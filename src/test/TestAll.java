package test;

import java.io.File;

import equations.EqSystem;
import tools.Parser;

public class TestAll {

	public static void main(String[] args) {
		Parser parser = new Parser();
		
		File folder = new File("C:\\Users\\Aurelien\\Documents\\KIT\\Cours\\Entscheidungsverfahren\\Programme\\KIT_LinDiophEq_Solver\\examples\\lin-eq-ex");
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				EqSystem system = parser.parse(listOfFiles[i].getPath());
				// System.out.println(system);
			}
		}
	}
}
