package instructions_selection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import Semantics.ClassEntry;
import assem.OPER;

import tree.*;

public class CodeGen extends AbstractCodeGen{
	HashMap <String, ClassEntry> symbolTable;
	List<Stm> methodStatements;
	String fragmentName;
	HashMap <TEMP, String> tempMap;
	
	public CodeGen (HashMap <String, ClassEntry> table) {
		symbolTable = table;
		tempMap = new HashMap <TEMP, String>();
	}
	
	public void setStatements (List <Stm> stms) {
		methodStatements = stms;
	}
	
	public void setFragmentName (String name) {
		fragmentName = name;
	}
	
	public void addTempToMap (TEMP t, String register) {
		tempMap.put(t, register);
	}
	
	public void muncher () {
		if(fragmentName.contains("main")) {
			emit(new OPER(".global start"));
			emit(new OPER(".align 8"));
			emit(new OPER("start:"));
		}
		if(!fragmentName.contains("main")) {
			emit(new OPER(fragmentName + ":"));
			emit(new OPER("save %sp, -96 & -8, %sp"));
		}
		for(Stm statement: methodStatements) {
			munchStm (statement);
		}
		emit(new OPER(fragmentName + "$epilogBegin:"));
		if(!fragmentName.contains("main")) {
			emit(new OPER("ret"));
			emit(new OPER("restore"));
		}
		else {
			emit(new OPER("call exit_program"));
		}
	}
	
	public void munchStm (Stm s) {
		if(s instanceof MOVE) {
			MOVE movStm = (MOVE) s;
			munchMove (movStm.dst, movStm.src);
		}
		else if (s instanceof CJUMP) {
			CJUMP cjumpStm = (CJUMP) s;
			munchCjump (cjumpStm);
		}
		else if (s instanceof JUMP) {
			JUMP jumpStm = (JUMP) s;
			munchJump (jumpStm);
		}
		else if (s instanceof EVAL) {
			EVAL evalStm = (EVAL) s;
			munchExp(evalStm.exp);
		}
		else if (s instanceof LABEL) {
			LABEL labelStm = (LABEL) s;
			munchLabel(labelStm);
		}
		else {
			//generic case
		}
	}
	
	public NameOfTemp munchExp (Exp e) { 
		if (e instanceof MEM) {
			MEM memExp = (MEM) e;
			return munchMem(memExp);
		}
		else if(e instanceof BINOP) {
			BINOP binopExp = (BINOP) e;
			return munchBinop (binopExp);
		}
		else if (e instanceof CALL) {
			CALL callExp = (CALL) e;
			return munchCall (callExp);
		}
		else if (e instanceof NAME) {
			NAME nameExp = (NAME) e;
			return munchName (nameExp);
		}
		else if (e instanceof TEMP) {
			TEMP tempExp = (TEMP) e;
			return tempExp.temp;
		}
		else if (e instanceof CONST) {
			CONST constExp = (CONST) e;
			return munchConst (constExp);
		}
		else {
			//generic case
		}
		return null;
	}
	
	public void munchMove (MEM dst, Exp src) {
		//MOVE(MEM(BINOP(PLUS, e1, CONST(i))), e2)
		if(dst.exp instanceof BINOP) {
			NameOfTemp dstTemp = munchExp((BINOP)dst.exp);
			NameOfTemp srcTemp = munchExp(src);
			emit(new OPER("mov " + dstTemp.toString() + ", " + srcTemp.toString()));
			//emit("STORE");
		}
		//MOVE(MEM(e1),MEM(e2))
		else if (src instanceof MEM) {
			NameOfTemp dstTemp = munchExp(dst.exp);
			NameOfTemp srcTemp = munchExp(((MEM)src).exp);
			emit(new OPER("mov " + dstTemp.toString() + ", " + srcTemp.toString()));
			//emit("MOVEM");
		}
		else {
			NameOfTemp dstTemp = munchExp(dst.exp);
			NameOfTemp srcTemp = munchExp(src);
			emit(new OPER("mov " + dstTemp.toString() + ", " + srcTemp.toString()));
			//emit("STORE");
		}
	}
	
	public void munchMove (TEMP dst, Exp src) {		
		if(src instanceof CALL) {
			NameOfTemp srcTemp = munchCall((CALL)src);
			String className = "";
			for(String key: symbolTable.keySet()) {
				ClassEntry currentClass = symbolTable.get(key);
				if(currentClass.getMethodContained(srcTemp.toString())) {
					className = key;
					break;
				}
			}
			emit(new OPER("mov " + srcTemp.toString() + ", " + dst.temp.toString()));
		}
		else {
			//MOVE(TEMP(t1), e)
			NameOfTemp srcTemp = munchExp(src);
			emit(new OPER("mov " + srcTemp.toString() + ", " + dst.temp.toString()));
			//emit("ADD");
		}
	}
	
	public void munchMove(Exp dst, Exp src) {
		//MOVE(d, e)
		if(dst instanceof MEM) {
			munchMove((MEM) dst, src);
		}
		else if (dst instanceof TEMP) {
			munchMove((TEMP) dst, src);
		}
	}
	
	public void munchLabel (LABEL l) {
		emit(new assem.LabelInstruction(l.label.toString() + ":", l.label));
	}
	
	public void munchJump (JUMP j) {
		emit(new assem.OPER("ba " + j.targets.get(0)));
		emit(new OPER("nop"));
	}

	public NameOfTemp munchCjump (CJUMP cj) {
		String branchOption = "";
		switch (cj.relop){
		case CJUMP.EQ:
			branchOption = "be ";
			break;
		case CJUMP.NE:
			branchOption = "bne ";
			break;
		case CJUMP.LT:
			branchOption = "bl ";
			break;
		case CJUMP.LE:
			branchOption = "ble ";
			break;
		case CJUMP.GT:
			branchOption = "bg ";
			break;
		case CJUMP.GE:
			branchOption = "be ";
			break;
		}
		//write compare statement
		NameOfTemp leftExp = munchExp(cj.left);
		NameOfTemp rightExp;
		if(cj.right instanceof CONST) {
			rightExp = new NameOfTemp(String.valueOf(((CONST)cj.right).value));
		}
		else {
			rightExp = munchExp(cj.right);
		}
		emit(new OPER("cmp " + leftExp.toString() + ", " + rightExp.toString()));
		emit(new OPER(branchOption + cj.iftrue));
		emit(new OPER("nop"));
		return null;
	}
	
	public NameOfTemp munchMem (MEM e) {
		if(e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.PLUS &&
				((BINOP)e.exp).right instanceof CONST) {
			return munchExp(((BINOP)e.exp).left);
			//emit("LOAD");
		}
		else if (e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.PLUS &&
				((BINOP)e.exp).left instanceof CONST) {
			return munchExp(((BINOP)e.exp).right);
			//emit("LOAD");
		}
		else if (e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.MINUS &&
				((BINOP)e.exp).right instanceof CONST) {
			return munchExp(((BINOP)e.exp).left);
		}
		else if (e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.MINUS &&
				((BINOP)e.exp).left instanceof CONST) {
			return munchExp(((BINOP)e.exp).right);
		}
		else if(e.exp instanceof CONST) {
			//emit("LOAD");
		}
		else {
			munchExp(e.exp);
			//emit("LOAD");
		}
		return null;
	}

	public NameOfTemp munchBinop (BINOP e) {
		if(e.binop == BINOP.PLUS) {
			if(e.left instanceof CONST && e.right instanceof CONST) {
				NameOfTemp result = NameOfTemp.generateTemp();
				int sum = ((CONST)e.left).value + ((CONST)e.right).value;
				String operation = "set " + sum + result.toString();
				emit(new OPER(operation));
				return result;
			}
			else if(e.right instanceof CONST) {
				NameOfTemp left = munchExp(e.left);
				String operation = "add " + left.toString() + ", " + (((CONST)e.right).value);
				emit(new OPER(operation));
				return left;
				//emit("ADDI");
			}
			else if (e.left instanceof CONST) {
				NameOfTemp right = munchExp(e.right);
				String operation = "add " + right.toString() + ", " + (((CONST)e.left).value);
				emit(new OPER(operation));
				return right;
				//emit("ADDI");
			}
			else {
				//emit("ADD");
			}
		}
		else if (e.binop == BINOP.MINUS) {
			if (e.left instanceof CONST && e.right instanceof CONST) {
				NameOfTemp result = NameOfTemp.generateTemp();
				int difference = ((CONST)e.left).value - ((CONST)e.right).value;
				String operation = "set " + difference + ", " + result.toString();
				emit(new OPER(operation));
				return result;
				//emit("SUB");
			}
			else if(e.right instanceof CONST) {
				NameOfTemp left = munchExp(e.left);
				String operation = "sub " + left.toString() + ", " + ((CONST)e.right).value + ", " + left.toString();
				emit(new OPER(operation));
				return left;
				//emit("SUBI");
			}
			else if (e.left instanceof CONST){
				
			}
		}
		else if (e.binop == BINOP.MUL) {
			NameOfTemp leftTemp = munchExp(e.left);
			NameOfTemp rightTemp = munchExp(e.right);
			String operation = "smul " + leftTemp.toString() + ", " + rightTemp.toString() + ", " + leftTemp.toString();
			emit(new OPER(operation));
			return leftTemp;
			//emit("SMUL")
		}
		return null;
	}

	public NameOfTemp munchName (NAME e) {
		return new NameOfTemp(e.label.toString());
		//return NameOfTemp.generateTemp();
	}
	
	public NameOfTemp munchConst (CONST c) {
		TEMP constTemp = TEMP.generateTEMP();
		emit(new OPER("set " + c.value + ", " + constTemp.temp.toString()));
		return constTemp.temp;
	}
	
	public NameOfTemp munchCall (CALL e) {
		NameOfTemp callName = munchExp(e.func);
		int indexNumber = 0;
		//emit(new OPER("mov		" + callName.toString() + ", %o0"));
		if(e.args != null) {
			for(Exp arg: e.args.toList()) {
				if(arg instanceof CONST) {
					emit(new OPER("set " + ((CONST)arg).value + ", %o" + indexNumber));
				}
				else {
					NameOfTemp argTemp = munchExp(arg);
					emit(new OPER("mov " + argTemp.toString() + ", %o" + indexNumber));
				}
				indexNumber++;
			}
		}
		String className = null;
		for(String key: symbolTable.keySet()) {
			ClassEntry currentClass = symbolTable.get(key);
			if(currentClass.getMethodContained(callName.toString())) {
				className = key;
				break;
			}
		}
		if (className != null) {
			emit(new OPER("call " + className + "$" + callName.toString()));
		}
		else {
			emit(new OPER("call " + callName.toString()));
		}
		emit(new OPER("nop"));
		return callName;
		//munchArgs (0, e.args);
	}
}
