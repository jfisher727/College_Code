package Semantics;

import java.util.HashMap;

public class ClassEntry {
	String className;
	String currentMethod;
	HashMap<String, VariableEntry> fields = new HashMap<String, VariableEntry>();
	HashMap<String, MethodEntry> methods = new HashMap<String, MethodEntry>();
	
	public ClassEntry (String name) {
		className = name;
		currentMethod = "";
	}
	
	public void setCurrentMethod (String method) {
		currentMethod = method;
	}
	
	public String getCurrentMethod () { 
		return currentMethod;
	}
	
	public boolean putField (String key, VariableEntry value) {
		if(fields.put(key, value) != null) {
			return false;
		}
		return true;
	}
	
	public boolean getFieldContained (String key) {
		return fields.containsKey(key);
	}
	
	public boolean getMethodContained (String key) {
		return methods.containsKey(key);
	}
	
	public boolean putMethod (String key, MethodEntry value) {
		if(methods.put(key, value) != null) {
			return false;
		}
		return true;
	}
	
	public VariableEntry getField (String key) {
		return fields.get(key);
	}
	
	public MethodEntry getMethod (String key) {
		return methods.get(key);
	}
	
	public String toString () {
		String returnString = className + " ";
		for(String key: fields.keySet()) {
			returnString += getField(key).toString() + " ";
		}
		for(String key: methods.keySet()) {
			returnString += getMethod(key).toString() + " ";
		}
		
		return returnString;
	}

}
