package instructions_selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import tree.NameOfTemp;
import tree.TEMP;

import assem.*;

public class AbstractCodeGen {
	ArrayList <Instruction> codeInstructions = new ArrayList <Instruction> ();
	ArrayList <String> formattedInstructions = new ArrayList <String> ();
	
	public void emit (Instruction s) {
		codeInstructions.add(s);
	}
	
	public void printCode () {
		for(Instruction i: codeInstructions) {
			System.out.println(i.toString());
		}
	}
	
	public void printFormatedCode () {
		for(String i: formattedInstructions) {
			System.out.println(i);
		}
	}
	
	public ArrayList <Instruction> getInstructions () {
		return codeInstructions;
	}
	
	public ArrayList <String> getFormattedInstructions () {
		return formattedInstructions;
	}
	
	public HashMap<NameOfTemp, String> generateMap () {
		HashMap<NameOfTemp, String> tempToRegisterMap = new HashMap<NameOfTemp, String>();
		String [] registers = {"%l1", "%l2", "%l3", "%l4", "%l5", "%l6", "%l7"};
		int currentIndex = 0;
		for(Instruction instruction: codeInstructions) {
			String tempName = "";
			String registerName = "";
			
			if(instruction.toString().contains("mov")) {
				if (instruction.toString().contains("t0")) {
					Scanner instructionScanner = new Scanner(instruction.toString());
					instructionScanner.next(); //mov
					String firstParameter = instructionScanner.next();
					String secondParameter = instructionScanner.next();
					if(firstParameter.contains("t0")) {
						//temp is the first parameter
						tempName = firstParameter.substring(0,firstParameter.length()-1); //cut off the ,
						if(secondParameter.contains("%")) {
							registerName = secondParameter;
						}
						else {
							registerName = registers[currentIndex];
							currentIndex++;
						}
						if(!tempToRegisterMap.containsKey(new NameOfTemp(tempName))) {
							tempToRegisterMap.put(new NameOfTemp(tempName), registerName);
						}
					}
					else if (secondParameter.contains("t0")) {
						tempName = secondParameter;
						if(!tempToRegisterMap.containsKey(new NameOfTemp(tempName))) {
							tempToRegisterMap.put(new NameOfTemp(tempName), registers[currentIndex]);
							currentIndex++;
						}
					}
					instructionScanner.close();
				}
			}
			else if (instruction.toString().contains("add")) {
				//should skip this
			}
			else if (instruction.toString().contains("smul")) {
				//should skip this
			}
			else if (instruction.toString().contains("sub")) {
				//should skip this
			}
			else if (instruction.toString().contains("eplilogBegin")) {
				currentIndex = 0;
			}
		}		
		return tempToRegisterMap;
	}
	
	public String formatMove (Instruction i, HashMap<NameOfTemp, String> map) {
		String formattedInstruction = "mov ";
		if (i.toString().contains("t0")) {
			Scanner instructionScanner = new Scanner(i.toString());
			instructionScanner.next(); //mov
			String firstParameter = instructionScanner.next();
			String secondParameter = instructionScanner.next();
			if(firstParameter.contains("t0")) {
				String tempName = firstParameter.substring(0, firstParameter.length() - 1);
				String associatedRegister = map.get(new NameOfTemp(tempName));
				formattedInstruction += associatedRegister + ", ";
			}
			else {
				formattedInstruction += firstParameter + " ";
			}
			if (secondParameter.contains("t0")) {
				String associatedRegister = map.get(new NameOfTemp(secondParameter));
				formattedInstruction += associatedRegister;
			}
			else {
				formattedInstruction += secondParameter;
			}
			instructionScanner.close();
		}
		else {
			formattedInstruction = i.toString();
		}
		return formattedInstruction;
	}
	
	public String formatAdd (Instruction i, HashMap<NameOfTemp, String> map) {
		String formattedInstruction = "add ";
		Scanner instructionScanner = new Scanner(i.toString());
		instructionScanner.next(); //add
		String firstParameter = instructionScanner.next();
		if(firstParameter.contains("t0")) {
			firstParameter = firstParameter.substring(0, firstParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(firstParameter));
			formattedInstruction += registerName + ", ";
		}
		else {
			formattedInstruction += firstParameter + " ";
		}
		String secondParameter = instructionScanner.next();
		if(secondParameter.contains("t0")) {
			secondParameter = secondParameter.substring(0, secondParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(secondParameter));
			formattedInstruction += registerName + ", ";
		}
		else {
			formattedInstruction += secondParameter + " ";
		}
		String thirdParameter = instructionScanner.next();
		if(thirdParameter.contains("t0")) {
			thirdParameter = thirdParameter.substring(0, thirdParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(thirdParameter));
			formattedInstruction += registerName;
		}
		else {
			formattedInstruction += thirdParameter;
		}				
		instructionScanner.close();
		return formattedInstruction;
	}
	
	public String formatMultiply (Instruction i, HashMap<NameOfTemp, String> map) {
		String formattedInstruction = "smul ";
		Scanner instructionScanner = new Scanner(i.toString());
		instructionScanner.next(); //add
		String firstParameter = instructionScanner.next();
		if(firstParameter.contains("t0")) {
			firstParameter = firstParameter.substring(0, firstParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(firstParameter));
			formattedInstruction += registerName + ", ";
		}
		else {
			formattedInstruction += firstParameter + " ";
		}
		String secondParameter = instructionScanner.next();
		if(secondParameter.contains("t0")) {
			secondParameter = secondParameter.substring(0, secondParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(secondParameter));
			formattedInstruction += registerName + ", ";
		}
		else {
			formattedInstruction += secondParameter + " ";
		}
		String thirdParameter = instructionScanner.next();
		if(thirdParameter.contains("t0")) {
			thirdParameter = thirdParameter.substring(0, thirdParameter.length());
			String registerName = map.get(new NameOfTemp(thirdParameter));
			formattedInstruction += registerName;
		}
		else {
			formattedInstruction += thirdParameter;
		}				
		instructionScanner.close();		
		return formattedInstruction;
	}
	
	public String formatSubtraction (Instruction i, HashMap<NameOfTemp, String> map) {
		String formattedInstruction = "sub ";
		Scanner instructionScanner = new Scanner(i.toString());
		instructionScanner.next(); //add
		String firstParameter = instructionScanner.next();
		if(firstParameter.contains("t0")) {
			firstParameter = firstParameter.substring(0, firstParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(firstParameter));
			formattedInstruction += registerName + ", ";
		}
		else {
			formattedInstruction += firstParameter + " ";
		}
		String secondParameter = instructionScanner.next();
		if(secondParameter.contains("t0")) {
			secondParameter = secondParameter.substring(0, secondParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(secondParameter));
			formattedInstruction += registerName + ", ";
		}
		else {
			formattedInstruction += secondParameter + " ";
		}
		String thirdParameter = instructionScanner.next();
		if(thirdParameter.contains("t0")) {
			thirdParameter = thirdParameter.substring(0, thirdParameter.length() - 1);
			String registerName = map.get(new NameOfTemp(thirdParameter));
			formattedInstruction += registerName;
		}
		else {
			formattedInstruction += thirdParameter;
		}				
		instructionScanner.close();
		return formattedInstruction;
	}
	
	public String formatSet (Instruction i, HashMap<NameOfTemp, String> map) {
		String formattedInstruction = "set ";
		if (i.toString().contains("t0")) {
			Scanner instructionScanner = new Scanner(i.toString());
			instructionScanner.next(); //set
			String firstParameter = instructionScanner.next();
			String secondParameter = instructionScanner.next();
			if(firstParameter.contains("t0")) {
				String tempName = firstParameter.substring(0, firstParameter.length() - 1);
				String associatedRegister = map.get(new NameOfTemp(tempName));
				formattedInstruction += associatedRegister + ", ";
			}
			else {
				formattedInstruction += firstParameter + " ";
			}
			if (secondParameter.contains("t0")) {
				String associatedRegister = map.get(new NameOfTemp(secondParameter));
				formattedInstruction += associatedRegister;
			}
			else {
				formattedInstruction += secondParameter;
			}
			instructionScanner.close();
		}
		else {
			formattedInstruction = i.toString();
		}
		return formattedInstruction;
	}
	
	public void formatInstructions () {
		HashMap <NameOfTemp, String> map = generateMap();
		for(Instruction i: codeInstructions) {
			String formattedInstruction = "";
			if(i.toString().contains("mov")) {
				formattedInstruction = formatMove (i, map);
			}
			else if (i.toString().contains("add")) {
				formattedInstruction = formatAdd(i, map);
			}
			else if (i.toString().contains("smul")) {
				formattedInstruction = formatMultiply(i, map);
			}
			else if (i.toString().contains("sub")) {
				formattedInstruction = formatSubtraction(i, map);
			}
			else if (i.toString().contains("set")) {
				formattedInstruction = formatSet(i, map);
			}
			else {
				formattedInstruction = i.toString();
			}
			formattedInstructions.add(formattedInstruction);
		}
	}
}
