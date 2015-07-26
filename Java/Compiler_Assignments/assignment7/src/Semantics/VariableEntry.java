package Semantics;

public class VariableEntry {
	String variableName;
	String variableType;
	
	public VariableEntry (String name, String type) {
		switch (type) {
		case ("int"):
			variableType = "IntegerType";
		break;
		case ("boolean"):
			variableType = "BooleanType";
		break;
		case("int[]"):
			variableType = "IntArrayType";
		break;
		default:
			variableType = type;
			break;
		}
		variableName = name;
	}
	
	public String toString () {
		return variableName + " " + variableType;
	}

}
