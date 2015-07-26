package main;

import instructions_selection.CodeGen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assem.Instruction;

import Semantics.ClassEntry;
import Semantics.ProgramVisitor;
import Semantics.TypeChecker;

import parser.ParseException;
import parser.Token;
import parser.parser;
import syntax.Program;
import translate.Translate;
import translate.ProcFrag;
import tree.Stm;
import tree.TreePrint;

public class Compiler {
	private static final String sparcFile = "assignment7.s";
	static HashMap <String, ClassEntry> symbolTable;
	static ArrayList <ProcFrag> codeFragments;
	static TypeChecker checker;
	static Translate translater;
	
	public static void finalPrintout (String filename, int errors) {
		System.out.println("filename=" + filename + ", errors=" + errors);
	}
	
	public static void writeToFileInstruction (ArrayList<Instruction> statements) {
		try {
			FileWriter sparcWriter = new FileWriter (new File(sparcFile));
			for(Instruction statement: statements) {
				sparcWriter.write(statement.toString());
				sparcWriter.write("\n");
			}
			sparcWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeToFileString (ArrayList <String> statements) {
		try {
			FileWriter sparcWriter = new FileWriter (new File(sparcFile));
			for(String statement: statements) {
				sparcWriter.write(statement.toString());
				sparcWriter.write("\n");
			}
			sparcWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			codeFragments = translater.getFragments();
			
			CodeGen codeMuncher = new CodeGen(symbolTable);
			for(ProcFrag fragment: codeFragments) {
				//System.out.println(tree.TreePrint.toString(fragment.body));
				List<Stm> statements = canon.Main.transform(fragment.body);
				/*
				for(Stm s: statements) {
					System.out.println(tree.TreePrint.toString(s));
				}
				*/
				codeMuncher.setStatements(statements);
				codeMuncher.setFragmentName(fragment.fragmentName);
				codeMuncher.muncher();
			}
			//codeMuncher.printCode();
			System.out.println();
			codeMuncher.formatInstructions();
			codeMuncher.printFormatedCode();
			
			
			writeToFileString(codeMuncher.getFormattedInstructions());
			
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
