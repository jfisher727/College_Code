package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import Semantics.ClassEntry;
import Semantics.ProgramVisitor;
import Semantics.TypeChecker;

import parser.ParseException;
import parser.Token;
import parser.parser;
import syntax.Program;
import translate.Translate;
import translate.ProcFrag;
import tree.TreePrint;

public class Parse {
	static HashMap <String, ClassEntry> symbolTable;
	static TypeChecker checker;
	static Translate translater;
	
	public static void finalPrintout (String filename, int errors) {
		System.out.println("filename=" + filename + ", errors=" + errors);
	}
	
	public static int parseFile (String filename) {
		int errors = 0;
		parser miniParser;
		try {
			
			ProgramVisitor visitor = new ProgramVisitor();
			miniParser = new parser(new FileInputStream(filename));
			Program miniJavaProg = miniParser.Goal();
			
			visitor.visit(miniJavaProg);
			symbolTable = visitor.getSymbolTable();
			
			checker = new TypeChecker (symbolTable);
			checker.visit(miniJavaProg);
			
			translater = new Translate(symbolTable);
			translater.visit(miniJavaProg);
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
		} catch (ParseException e) {
			Token t = e.currentToken;
			System.err.println(filename + ":" + t.beginLine + "." + t.beginColumn + ": Syntax ERROR: " + e.getMessage());
			errors++;
			//finalPrintout(filename, errors);
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
			/*
			if(checker.numberOfErrors() >= 1) {
				checker.printErrors();
			}
			*/
		}
	}
}
