package Semantics;

import java.util.HashMap;
import java.util.List;

import syntax.And;
import syntax.ArrayAssign;
import syntax.ArrayLength;
import syntax.ArrayLookup;
import syntax.Assign;
import syntax.Block;
import syntax.BooleanType;
import syntax.Call;
import syntax.ClassDecl;
import syntax.ExtendingClassDecl;
import syntax.False;
import syntax.Formal;
import syntax.Identifier;
import syntax.IdentifierExp;
import syntax.IdentifierType;
import syntax.If;
import syntax.IntArrayType;
import syntax.IntegerLiteral;
import syntax.IntegerType;
import syntax.LessThan;
import syntax.MainClass;
import syntax.MethodDecl;
import syntax.Minus;
import syntax.NewArray;
import syntax.NewObject;
import syntax.Not;
import syntax.Plus;
import syntax.Print;
import syntax.Program;
import syntax.SimpleClassDecl;
import syntax.SyntaxTreeVisitor;
import syntax.This;
import syntax.Times;
import syntax.True;
import syntax.VarDecl;
import syntax.While;

public class ProgramVisitor implements SyntaxTreeVisitor <Void>{
	HashMap <String, ClassEntry> classes = new HashMap<String, ClassEntry>();
	String currentClass;
	
	public HashMap<String,ClassEntry> getSymbolTable () {
		return classes;
	}

	@Override
	public Void visit(Program n) {
		if(n==null) {
			//null program
		}
		else if(n.m == null) {
			//null main class
		}
		else {
			n.m.accept(this);
			for(ClassDecl c: n.cl) {
				c.accept(this);
			}
		}
		return null;
	}

	@Override
	public Void visit(MainClass n) {
		ClassEntry currentClass = new ClassEntry(n.i1.toString());
		if(classes.put(currentClass.className, currentClass) != null) {
			//error
			return null;
		}
		this.currentClass = currentClass.className;
		//System.out.println("Main Class: " + currentClass.toString());
		return null;
	}

	@Override
	public Void visit(SimpleClassDecl n) {
		ClassEntry currentClass = new ClassEntry(n.i.toString());
		if(classes.put(currentClass.className, currentClass) != null) {
			//error
			return null;
		}
		this.currentClass = currentClass.className;
		List<VarDecl> vars = n.vl;
		for(VarDecl var : vars) {
			var.accept(this);
		}
		List<MethodDecl> methods = n.ml;
		for(MethodDecl method: methods) {
			method.accept(this);
		}
		//currentClass = classes.get(currentClass);
		//System.out.println(currentClass.toString());
		return null;
	}

	@Override
	public Void visit(ExtendingClassDecl n) {
		ClassEntry currentClass = new ClassEntry(n.i.toString());
		if(classes.put(currentClass.className, currentClass) != null) {
			//error
			return null;
		}
		this.currentClass = currentClass.className;
		List<VarDecl> vars = n.vl;
		for(VarDecl var : vars) {
			var.accept(this);
		}
		List<MethodDecl> methods = n.ml;
		for(MethodDecl method: methods) {
			method.accept(this);
		}
		return null;
	}

	@Override
	public Void visit(VarDecl n) {
		ClassEntry currentClassEntry = classes.get(currentClass);
		VariableEntry fieldEntry = new VariableEntry(n.i.toString(), n.t.toString());
		String currentMethodName = currentClassEntry.getCurrentMethod();
		if(currentMethodName.equals("")) { //no method name assigned
			if(!currentClassEntry.putField(fieldEntry.variableName, fieldEntry)) {
				//error
				return null;
			}
		}
		else {
			MethodEntry currentMethod = currentClassEntry.getMethod(currentMethodName);
			if(!currentMethod.putLocal(fieldEntry.variableName, fieldEntry)) {
				return null; //error
			}
		}
		classes.remove(currentClass);
		classes.put(currentClass, currentClassEntry);
		return null;
	}

	@Override
	public Void visit(MethodDecl n) {
		ClassEntry currentClassEntry = classes.get(currentClass);
		MethodEntry currentMethod = new MethodEntry (n.i.toString(), n.t.toString());
		//add method return type to method entry
		if(n.t.toString().equals("int")) {
			currentMethod.resultType = "IntegerType";
		}
		else if(n.t.toString().equals("boolean")) {
			currentMethod.resultType = "BooleanType";
		}
		else if(n.t.toString().equals("int[]")) {
			currentMethod.resultType = "IntArrayType";
		}
		currentClassEntry.setCurrentMethod(currentMethod.methodName);
		if(!currentClassEntry.putMethod(currentMethod.methodName, currentMethod)) {
			return null; //error
		}
		List<Formal> formals = n.fl;
		for(Formal formal: formals) {
			formal.accept(this);
		}
		List<VarDecl> vars = n.vl;
		for(VarDecl var: vars) {
			var.accept(this);
		}
		return null;
	}

	@Override
	public Void visit(Formal n) {
		ClassEntry currentClassEntry = classes.get(currentClass);
		MethodEntry currentMethodEntry = currentClassEntry.getMethod(currentClassEntry.getCurrentMethod());
		VariableEntry currentVariable = new VariableEntry(n.i.toString(), n.t.toString());
		if(!currentMethodEntry.putParam(currentVariable.variableName, currentVariable)) {
			return null; //error
		}
		return null;
	}

	@Override
	public Void visit(IntArrayType n) {
		return null;
	}

	@Override
	public Void visit(BooleanType n) {
		return null;
	}

	@Override
	public Void visit(IntegerType n) {
		return null;
	}

	@Override
	public Void visit(IdentifierType n) {
		return null;
	}

	@Override
	public Void visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(If n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Assign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(True n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(False n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Not n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}

}
