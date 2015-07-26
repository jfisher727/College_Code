package main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import parser.Token;
import parser.scanner;
import parser.scannerConstants;


public class Scan {
	
	public static void finalPrintout (String filename, int errors) {
		System.out.println("filename=" + filename + ", errors=" + errors);
	}

	public static void main (String [] args) {
		System.getProperties();
		String property = System.getProperty("verbose");
		int errors = 0;
		String filename = args [0];
		scanner lexer;
		try {
			lexer = new scanner (new FileInputStream(filename));
			for(;;) {
				final Token t = lexer.getNextToken ();
				if(t.kind == scannerConstants.EOF) {
					finalPrintout(filename, errors);
					return;
				}
				else if (t.kind == scannerConstants.INVALID) {
					System.err.println(filename + ":" + t.beginLine + "." + t.beginColumn + ": ERROR -- illegal  character " + t.image);
					//invalid token need to add as an error
					errors++;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
