package Semantics;

import java.util.ArrayList;

public class ErrorHandling {
	ArrayList<String> errors = new ArrayList<String>();
	
	ErrorHandling() {
		
	}
	
	public void addError(String error) {
		errors.add(error);
	}
	
	public void printErrors () {
		for(String error: errors) {
			System.err.println(error);
		}
	}

}
