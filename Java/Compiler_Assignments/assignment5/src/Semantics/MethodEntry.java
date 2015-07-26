package Semantics;

import java.util.HashMap;

public class MethodEntry {
	String methodName;
	String resultType;
	HashMap<String, VariableEntry> params = new HashMap<String, VariableEntry>();
	HashMap<String, VariableEntry> locals = new HashMap<String, VariableEntry>();
	
	public MethodEntry (String name, String result) {
		methodName = name;
		resultType = result;
	}
	
	public boolean putParam (String key, VariableEntry value) {
		if(params.put(key, value) != null) {
			return false;
		}
		return true;
	}
	
	public boolean putLocal (String key, VariableEntry value) {
		if(locals.put(key, value) != null) {
			return false;
		}
		return true;
	}
	
	public boolean getParamContained (String key) {
		return params.containsKey(key);
	}
	
	public boolean getLocalContained (String key) {
		return locals.containsKey(key);
	}
	
	public VariableEntry getParam (String key) {
		return params.get(key);
	}
	
	public VariableEntry getLocal (String key) {
		return locals.get(key);
	}
	
	public String toString () {
		String returnString = methodName + " " + resultType + " Parameters: ";
		for(String key: params.keySet()) {
			returnString += getParam(key).toString() + " ";
		}
		returnString += " Locals: ";
		for(String key: locals.keySet()) {
			returnString += getLocal(key).toString() + " ";
		}
		return returnString;
	}
}
