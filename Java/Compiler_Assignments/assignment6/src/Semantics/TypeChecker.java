package Semantics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import syntax.*;

public class TypeChecker implements SyntaxTreeVisitor <String>{
	HashMap<String, ClassEntry> symbolTable;
	ErrorHandling error;
	String currentClassName;
	
	public TypeChecker (HashMap<String, ClassEntry> map) {
		this.symbolTable = map;
		this.error = new ErrorHandling();
	}

	public void printErrors () {
		error.printErrors();
	}
	
	public int numberOfErrors () {
		return error.errors.size();
	}
	
	@Override
	//main class
	//list of class decl
	public String visit(Program n) {
		n.m.accept(this); //visit main class
		for(ClassDecl decl: n.cl) {
			decl.accept(this); //visit all other classes
		}
		return null;
	}

	@Override
	//identifier i1 = name
	//identifier i2 = arguments
	//statement s
	public String visit(MainClass n) {
		//only check statement for mainclass
		n.s.accept(this);
		return null;
	}

	@Override
	//identifier i
	//list of vardecl
	//list of methoddecl
	public String visit(SimpleClassDecl n) {
		currentClassName = n.i.s;
		for(VarDecl var: n.vl) {
			var.accept(this);
		}
		for(MethodDecl method: n.ml) {
			method.accept(this);
		}
		return null;
	}

	@Override
	//identifier i1
	//identifier i2
	//list of vardecl
	//list of methoddecl
	public String visit(ExtendingClassDecl n) {
		currentClassName = n.i.s;
		for(VarDecl var: n.vl) {
			var.accept(this);
		}
		for(MethodDecl method: n.ml) {
			method.accept(this);
		}
		return null;
	}

	@Override
	//identifier i
	//type t
	public String visit(VarDecl n) {
		ClassEntry currentClass = symbolTable.get(currentClassName);
		VariableEntry var = new VariableEntry(n.i.s, n.t.getName());
		if(!currentClass.getFieldContained(n.i.s)) {
			MethodEntry currentMethod = currentClass.getMethod(currentClass.currentMethod);
			if(!currentMethod.getParamContained(n.i.s) && !currentMethod.getLocalContained(n.i.s)) {
				error.addError("Variable " + n.i.s + " was not defined at location: " + n.i.lineNumber + ":" + n.i.columnNumber);
				currentClass.putField(n.i.s, var);
			}
		}
		return var.variableType;
	}

	@Override
	//type t - return type
	//identifier i - method name
	//list of formals - parameters
	//list of variables
	//list of statements
	//expression e
	public String visit(MethodDecl n) {
		ClassEntry currentClass = symbolTable.get(currentClassName);
		MethodEntry method = new MethodEntry (n.i.s, n.t.getName());
		currentClass.currentMethod = method.methodName;
		if(!currentClass.getMethodContained(method.methodName)) {
			error.addError("Method " + method.methodName + " was not defined at location: " + n.t.lineNumber + ":" + n.t.columnNumber);
			currentClass.putMethod(method.methodName, method);
		}
		for(Formal formal: n.fl) {
			formal.accept(this);
		}
		for(VarDecl var: n.vl) {
			var.accept(this);
		}
		for(Statement statement: n.sl) {
			statement.accept(this);
		}
		return null;
	}

	@Override
	//type t
	//identifier i
	public String visit(Formal n) {
		ClassEntry currentClass = symbolTable.get(currentClassName);
		MethodEntry currentMethod = currentClass.getMethod(currentClass.currentMethod);
		VariableEntry var = new VariableEntry (n.i.s, n.t.getName());
		if(!currentMethod.getParamContained(n.i.s)) {
			error.addError("Formal " + var.variableName + " listed as a parameter in method: " + currentMethod.methodName + " at location: " + n.t.lineNumber + ":" + n.t.columnNumber);
			currentMethod.putParam(var.variableName, var);
		}
		return var.variableType;
	}

	@Override
	public String visit(IntArrayType n) {
		return "IntArrayType";
	}

	@Override
	public String visit(BooleanType n) {
		return "BooleanType";
	}

	@Override
	public String visit(IntegerType n) {
		return "IntegerType";
	}

	@Override
	public String visit(IdentifierType n) {
		return "IdentifierType";
	}

	@Override
	//list of statements
	public String visit(Block n) {
		for(Statement s: n.sl) {
			s.accept(this);
		}
		return null;
	}

	@Override
	//expression e
	//statement s1
	//statement s2
	public String visit(If n) {
		if(!n.e.accept(this).equals("BooleanType")) {
			error.addError("If expression located at: " + n.e.lineNumber + ":" + n.e.columnNumber + " doesn't evaluate to a boolean.");
		}
		n.s1.accept(this);
		n.s2.accept(this);
		return "BooleanType";
	}

	@Override
	//expression e
	//statement s
	public String visit(While n) {
		if(!n.e.accept(this).equals("BooleanType")) {
			error.addError("While expression located at: " + n.e.lineNumber + ":" + n.e.columnNumber + " doesn't evaluate to a boolean.");
		}
		n.s.accept(this);
		return "BooleanType";
	}

	@Override
	//expression e
	public String visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//identifier i
	//expression e
	public String visit(Assign n) {
		ClassEntry currentClass = symbolTable.get(currentClassName);
		MethodEntry currentMethod = currentClass.getMethod(currentClass.currentMethod);
		if(!currentClass.getFieldContained(n.i.s)) {
			if(!currentMethod.getParamContained(n.i.s)) {
				if(!currentMethod.getLocalContained(n.i.s)) {
					error.addError("Assignment for variable named " + n.i.s + " located at: " + n.i.lineNumber + ":" + n.i.columnNumber + "is not initalized as a parameter or a local.");
				}
				else {
					//the assignment is for a local
					String expressionType = n.e.accept(this);
					if(!currentMethod.getLocal(n.i.s).variableType.equals(expressionType)) {
						error.addError("Assignment for variable named " + n.i.s + " located at: " + n.i.lineNumber + ":" + n.i.columnNumber + "has mismatched types.");
					}
				}
			}
			else {
				//the assignment is for a formal
				String expressionType = n.e.accept(this);
				if(!currentMethod.getParam(n.i.s).variableType.equals(expressionType)) {
					error.addError("Assignment for variable named " + n.i.s + " located at: " + n.i.lineNumber + ":" + n.i.columnNumber + "has mismatched types.");
				}
			}
		}
		return null;
	}

	@Override
	//identifier i
	//expression e1
	//expression e2
	public String visit(ArrayAssign n) {
		if(!n.e1.accept(this).equals("IntegerType")) {
			error.addError("Expression 1 in the Array Assign located on " + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to an integer type.");
		}
		if(!n.e2.accept(this).equals("IntegerType")) {
			error.addError("Expression 2 in the Array Assign located on " + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to an integer type.");
		}
		return "IntegerType";
	}

	@Override
	//expression e1
	//expression e2
	public String visit(And n) {
		boolean errorIn = false;
		if(!n.e1.accept(this).equals("BooleanType")) {
			error.addError("Expression 1 in the And located on " + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to a boolean type.");
			errorIn = true;
		}
		if(!n.e2.accept(this).equals("BooleanType")) {
			error.addError("Expression 2 in the And located on " + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to a boolean type.");
			errorIn = true;
		}
		if(!errorIn) {
			return "BooleanType";
		}
		return null;
	}

	@Override
	//expression e1
	//expression e2
	public String visit(LessThan n) {
		boolean errorIn = false;
		if(!n.e1.accept(this).equals("IntegerType")) {
			error.addError("Expression 1 in the Less Than located on " + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to an integer type.");
			errorIn = true;
		}
		if(!n.e2.accept(this).equals("IntegerType")) {
			error.addError("Expression 2 in the Less Than located on " + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to an integer type.");
			errorIn = true;
		}
		if(!errorIn) {
			return "BooleanType";
		}
		return null;
	}

	@Override
	//expression e1
	//expression e2
	public String visit(Plus n) {
		if(!n.e1.accept(this).equals("IntegerType")) {
			error.addError("Expression 1 in the Plus located on " + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to an integer type.");
		}
		if(!n.e2.accept(this).equals("IntegerType")) {
			error.addError("Expression 2 in the Plus located on " + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to an integer type.");
		}
		return "IntegerType";
	}

	@Override
	//expression e1
	//expression e2
	public String visit(Minus n) {
		if(!n.e1.accept(this).equals("IntegerType")) {
			error.addError("Expression 1 in the Minus located on " + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to an integer type.");
		}
		if(!n.e2.accept(this).equals("IntegerType")) {
			error.addError("Expression 2 in the Minus located on " + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to an integer type.");
		}
		return "IntegerType";
	}

	@Override
	//expression e1
	//expression e2
	public String visit(Times n) {
		if(!n.e1.accept(this).equals("IntegerType")) {
			error.addError("Expression 1 in the Multiplication located on " + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to an integer type.");
		}
		if(!n.e2.accept(this).equals("IntegerType")) {
			error.addError("Expression 2 in the Multiplication located on " + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to an integer type.");
		}
		return "IntegerType";
	}

	@Override
	//expression e1
	//expression e2
	public String visit(ArrayLookup n) {
		/*
		if(!n.e1.accept(this).equals("IntegerType")) {
			error.addError("Expression 1 in the Less Than located on" + n.e1.lineNumber + ":" + n.e1.columnNumber + " doesn't evaluate to an integer type.");
		}
		*/
		if(!n.e2.accept(this).equals("IntegerType")) {
			error.addError("Expression 2 in the Array Lookup located on" + n.e2.lineNumber + ":" + n.e2.columnNumber + " doesn't evaluate to an integer type.");
		}
		return "IntegerType";
	}

	@Override
	//expression e1
	public String visit(ArrayLength n) {
		n.e.accept(this);
		return null;
	}

	@Override
	//expression e
	//identifier i
	//list of expression
	public String visit(Call n) {
		for(String curClassName: symbolTable.keySet()) {
			ClassEntry currentClass = symbolTable.get(curClassName);
			if(currentClass.getMethodContained(n.i.s)) {
				MethodEntry currentMethod = currentClass.getMethod(n.i.s);
				Set<String> parameters = currentMethod.params.keySet();
				Iterator<String> paramIterator = parameters.iterator();
				if(parameters.size() == n.el.size()) {
					for(Expression exp: n.el ){
						VariableEntry curParam = currentMethod.getParam(paramIterator.next());
						if(!exp.accept(this).equals(curParam.variableType)) {
							//error
							error.addError("Parameters for " + currentMethod.methodName + " don't match the parameters passed");
							return null;
						}
					}
					return currentMethod.resultType;
				}
				else {
					continue;
				}
			}
		}
		return null;
	}

	@Override
	public String visit(IntegerLiteral n) {
		return "IntegerType";
	}

	@Override
	public String visit(True n) {
		return "BooleanType";
	}

	@Override
	public String visit(False n) {
		return "BooleanType";
	}

	@Override
	public String visit(IdentifierExp n) {
		ClassEntry currentClass = symbolTable.get(currentClassName);
		MethodEntry currentMethod = currentClass.getMethod(currentClass.currentMethod);
		if(!currentClass.getFieldContained(n.s)) {
			if(!currentMethod.getParamContained(n.s)) {
				if(!currentMethod.getLocalContained(n.s)){
					return null;
				}
				else {
					return currentMethod.getLocal(n.s).variableType;
				}
			}
			else {
				return currentMethod.getParam(n.s).variableType;
			}
		}
		else {
			return currentClass.getField(n.s).variableType;
		}
	}

	@Override
	public String visit(This n) {
		ClassEntry currentClass = symbolTable.get(currentClassName);
		return currentClass.className;
	}

	@Override
	public String visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//expression e
	public String visit(Not n) {
		return n.e.accept(this);
	}

	@Override
	public String visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}

}
