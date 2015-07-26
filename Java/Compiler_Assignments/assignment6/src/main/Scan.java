package main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import parser.Token;
import parser.parser;
import parser.parserConstants;


public class Scan {
	
	public void finalPrintout (String filename, int errors) {
		System.out.println("filename=" + filename + ", errors=" + errors);
	}
	
	public int scan (String filename) {
		System.getProperties();
		String property = System.getProperty("verbose");
		int errors = 0;
		//String filename = args [0];
		parser lexer;
		try {
			lexer = new parser (new FileInputStream(filename));
			for(;;) {
				final Token t = lexer.getNextToken ();
				if(t.kind == parserConstants.EOF) {
					return errors;
				}
				else if (t.kind == parserConstants.INVALID) {
					System.err.println(filename + ":" + t.beginLine + "." + t.beginColumn + ": ERROR -- illegal  character " + t.image);
					//invalid token need to add as an error
					errors++;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static void main (String [] args) {
			
	}
}
