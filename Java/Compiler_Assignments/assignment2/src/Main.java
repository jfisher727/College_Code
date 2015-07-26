/*
 * Author: Jason Fisher, jfisher2009@my.fit.edu
 * Assignment: Assign2
 * Course: CSE 4251
 */

import java.util.Scanner;


public class Main {
	class R {
		int a, b, c;
		final int [] y = new int [10];
	}	
		
	public static void startOfAssembly() {
		System.out.println(".section	\".rodata\"");
		System.out.println(".align		8");
		System.out.println("Var_A:	.word		0");
		System.out.println("Var_B:	.word		0");
		System.out.println("Var_C:	.word		0");
		//declares an array of size 10 with all 0's inside
		System.out.println("Array:	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println("	.word		0");
		System.out.println();
		
		System.out.println(".set SYS_exit, 1");
		System.out.println(".macro exit_program");
		System.out.println("clr %o0");
		System.out.println("mov SYS_exit, %g1");
		System.out.println("ta 0");
		System.out.println(".endm");
		System.out.println();
		
		System.out.println(".section	\".text\"");
		System.out.println(".align		4");
		
		System.out.println();
		System.out.println(".global		start");
		System.out.println("start:");
	}
	
	public static R updateValues (String input, R storage) {
		char firstChar = input.charAt(0);
		String firstCharacter = input.substring(0, 1);
		String secondCharacter = input.substring(2);
		int valueOfSecond = Integer.valueOf(secondCharacter);
		/*
		 * first character of input is a letter, so we
		 * know it's being assigned the value of the second
		 * Character
		 */
		if(Character.isLetter(firstChar)) {
			switch (firstChar) {
			case 'a':
				storage.a = valueOfSecond;
				System.out.println("set Var_A, %l0");
				System.out.println("mov " + storage.a + ", %l1");
				System.out.println("st %l1, [%l0]");
				System.out.println();
				break;
			case 'b':
				storage.b = valueOfSecond;
				System.out.println("set Var_B, %l0");
				System.out.println("mov " + storage.b + ", %l1");
				System.out.println("st %l1, [%l0]");
				System.out.println();
				
				break;
			case 'c':
				storage.c = valueOfSecond;
				System.out.println("set Var_C, %l0");
				System.out.println("mov " + storage.c + ", %l1");
				System.out.println("st %l1, [%l0]");
				System.out.println();
				break;
			default:
				break;
			}
		}
		/*
		 * we know that if the first character is a number,
		 * the array at the specified position is being set to
		 * the second value
		 */
		else if(Character.isDigit(firstChar)) {
			int arrayIndex = Integer.valueOf(firstCharacter);
			if(arrayIndex < storage.y.length) {
				storage.y[arrayIndex] = valueOfSecond;
				System.out.println("set " + arrayIndex + ", %l0");
				System.out.println("set Array, %l2");
				System.out.println("sll %l0, 2, %l4");
				System.out.println("mov " + valueOfSecond + ", %l6");
				System.out.println("st %l6, [%l4+%l2]");
				System.out.println();
			}
		}
		return storage;
	}
	
	public static void printValue (String input, R storage) {
		char onlyCharacter = input.charAt(0);
		//if the character is a letter
		if(Character.isLetter(onlyCharacter)) {
			switch (onlyCharacter) {
			case 'a':
				System.out.println("mov	Var_A,%o0");
				System.out.println("call println");
				System.out.println("nop");
				System.out.println();
				//int aValue = storage.a;
				//System.out.println(aValue);
				break;
			case 'b':
				//int bValue = storage.b;
				//System.out.println(bValue);
				System.out.println("mov Var_B,%o0");
				System.out.println("call println");
				System.out.println("nop");
				System.out.println();
				break;
			case 'c':
				//int cValue = storage.c;
				//System.out.println(cValue);
				System.out.println("mov	Var_B,%o0");
				System.out.println("call println");
				System.out.println("nop");
				System.out.println();
				break;
			default:
				break;
			}
		}
		//if the character is a number
		else if (Character.isDigit(onlyCharacter)) {
			int arrayIndex = Integer.valueOf(input);
			//System.out.println(storage.y[valueOfChar]);
			if (arrayIndex < storage.y.length) {
				System.out.println("set " + arrayIndex + ", %l0");
				System.out.println("set Array, %l2");
				System.out.println("sll %l0, 2, %l4");
				System.out.println("ld [%l4+%l2], %o0");
				System.out.println("call	println");
				System.out.println("nop");
				System.out.println();
			}
		}
	}
	
	public static void readFromInput (R storage) {
		Scanner stdInput = new Scanner(System.in);
		//keep reading from standard input until there are no more lines
		while(stdInput.hasNextLine()) {
			String newestInput = stdInput.nextLine();
			/*
			 * if the line is longer than one character,
			 * we know there will be 2 with a white space between
			 */
			if(newestInput.length() > 1) {
				storage = updateValues (newestInput, storage);
			}
			else {
				printValue (newestInput, storage);
			}
		}
		stdInput.close();
	}
	
	public static void endOfAssembly () {
		System.out.println("exit_program");
	}
	
	public static void main(String[] args) {
		Main instance = new Main ();
		R r = instance.new R ();
		startOfAssembly ();
		readFromInput (r);
		endOfAssembly ();
	}

}
