import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Scanner;

/*
 * Author: Jason Fisher, jfisher2009@my.fit.edu
 * Author: Chenke Li, williamkeke0@gmail.com
 * Course: CSE 4051, Fall 2013
 * Project: proj2, Propositional Checker
 */

public class Checker {
	private static final int MAXUNIQUECHARACTERS = 6;
	private static final int POSSIBLECOMBINATIONS = 64;

	private static char [] charactersCollected;

	/*
	 * Generates a unique bitset given a particular "identifier"
	 * this is used to make sure that all possible combinations of true and false
	 * are generated for up to 6 different letter combinations
	 */
	public static BitSet generateBitset (final int uniqueCount) {
		final BitSet combinations = new BitSet(POSSIBLECOMBINATIONS);
		final int midpoints = (int)
				(POSSIBLECOMBINATIONS / (Math.pow(2, uniqueCount + 1)));
		boolean trueOrFalse = true;
		for (int i = 0; i < POSSIBLECOMBINATIONS; i++) {
			if (i % midpoints == 0) {
				trueOrFalse = !trueOrFalse;
			}
			//if the bit needs to be set
			if (trueOrFalse) {
				combinations.set(i);
			}
		}
		return combinations;
	}

	/*
	 * Iterates through the two bitsets checking to see if any bits are equivilent,
	 * if any are, such as both are set to 1 or 0, then a resulting bitset is "set"
	 * at that particular index
	 */
	public static BitSet checkForEquivilence
	(final BitSet first, final BitSet second) {
		final BitSet result = new BitSet(POSSIBLECOMBINATIONS);
		for (int i = 0; i < first.size(); i++) {
			   if (first.get(i) == second.get(i)) {
				   result.set(i);
			   }
		}
		return result;
	}

	/*
	 * Iterates through each of the bitsets checking to see if they follow the basic
	 * rules of implication, if the first is false the resulting bitset is automatically
	 * set to true, and if the first is true then the second needs to also be true
	 * in order for the resulting bitset to be set true at that particular index
	 */
	public static BitSet checkForImplication
	(final BitSet first, final BitSet second) {
		final BitSet result = new BitSet(POSSIBLECOMBINATIONS);
		for (int i = 0; i < first.size(); i++) {
			final boolean valueOfFirst = first.get(i);
			/*
			 * if the first proposition is true, the second must be
			 * true in order for the resulting proposition to be true
			 */
			if (valueOfFirst) {
				final boolean valueOfSecond = second.get(i);
				if (valueOfSecond) {
					result.set(i);
				}
			}
			else {
				result.set(i);
			}
		}
		return result;
	}

	public static BitSet processOperation
	(final ArrayDeque<BitSet> stack, final char opperation) {
		BitSet topOfStack = null, secondElement;
		switch (opperation) {
		case '-':
			//negate most recent character
			topOfStack = stack.pop();
			//flip the entire bitset
			topOfStack.flip(0, POSSIBLECOMBINATIONS);
			break;
		case '&':
			//and the last two characters together
			topOfStack = stack.pop();
			secondElement = stack.pop();
			topOfStack.and(secondElement);
			break;
		case '|':
			//or the last two characters together
			topOfStack = stack.pop();
			secondElement = stack.pop();
			topOfStack.or(secondElement);
			break;
		case '=':
			//check the last two characters for equivilence
			topOfStack = stack.pop();
			secondElement = stack.pop();
			topOfStack = checkForEquivilence(topOfStack, secondElement);
			break;
		case '>':
			topOfStack = stack.pop();
			secondElement = stack.pop();
			topOfStack = checkForImplication(secondElement, topOfStack);
			//check the last two characters for implication
			break;
		default:
			break;
		}
		return topOfStack;
	}

	/*
	 * Takes the line of input from the user and begins parsing it
	 * character by character, this is the main handler for when a 
	 * statement is submitted
	 */
	public static void parseProposition (final String proposition) {
		charactersCollected = new char [MAXUNIQUECHARACTERS];
		final ArrayDeque<BitSet> stack = new ArrayDeque <BitSet> ();
		int uniqueCharacters = 0, index = 0;
		while ((uniqueCharacters < MAXUNIQUECHARACTERS)
				&& (index < proposition.length())) {
			final char currentCharacter = proposition.charAt(index);
			if (Character.isLetter(currentCharacter)) {
				BitSet currentCombination = null;
				for (int i = 0; i < uniqueCharacters; i++) {
					if (currentCharacter == charactersCollected [i]) {
						currentCombination = generateBitset (i);
						stack.push(currentCombination);
						break;
					}
				}
				//if the character is unique, generate a new combination for that character
				if (currentCombination == null) {
					charactersCollected [uniqueCharacters]
							= currentCharacter;
					currentCombination
					= generateBitset (uniqueCharacters);
					uniqueCharacters++;
					stack.push(currentCombination);
				}
			}
			//the character is not a letter, therefore it must be one of the propositional characters
			else {
				if (currentCharacter != ' ') {
					final BitSet operationResult
					= processOperation (stack, currentCharacter);
					stack.push(operationResult);
				}
			}
			index++;
		}
		//print if the statement is a tautology, contingent, or contradiction
		final BitSet result = stack.pop();
		if (result.cardinality() == POSSIBLECOMBINATIONS) {
			System.out.println("tautology");
		}
		else if (result.cardinality() == 0) {
			System.out.println("contradiction");
		}
		else {
			System.out.println("contingent");
		}
	}

	public static void collectPropositions () {
		final Scanner stdInput = new Scanner (System.in);
		while (stdInput.hasNextLine()) {
			final String currentProposition = stdInput.nextLine();
			parseProposition (currentProposition);
		}
		stdInput.close();
	}

	public static void main (final String [] args) {
		collectPropositions ();
	}
}
