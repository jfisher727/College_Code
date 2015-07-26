package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import parser.ParseException;
import parser.Token;
import parser.parser;
import parser.parserConstants;

public class Parse {
	public static void finalPrintout (String filename, int errors) {
		System.out.println("filename=" + filename + ", errors=" + errors);
	}
	
	public static int parseFile (String filename) {
		int errors = 0;
		try {
			parser parse = new parser(new FileInputStream(filename));
			parse.Goal();
			return errors;
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
		} catch (ParseException e) {
			Token t = e.currentToken;
			System.err.println(filename + ":" + t.beginLine + "." + t.beginColumn + ": Syntax ERROR: " + e.getMessage());
			errors++;
			//e.printStackTrace();
		}
		return errors;
		
	}

	public static void main (String [] args) {
		if(args.length < 1) {
			System.out.println("Need to enter filename as first arg");
		}
		else {
			String filename = args [0];
			int errors = parseFile(filename);
			if(errors >= 0) {
				finalPrintout(filename, errors);
			}
			else {
				System.out.println("Some error occured.");
			}
		}
	}
}
