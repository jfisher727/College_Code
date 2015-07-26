package translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Semantics.ClassEntry;
import Semantics.MethodEntry;

import syntax.And;
import syntax.ArrayAssign;
import syntax.ArrayLength;
import syntax.ArrayLookup;
import syntax.Assign;
import syntax.Block;
import syntax.BooleanType;
import syntax.Call;
import syntax.ClassDecl;
import syntax.Expression;
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
import syntax.Statement;
import syntax.SyntaxTreeVisitor;
import syntax.This;
import syntax.Times;
import syntax.True;
import syntax.VarDecl;
import syntax.While;
import tree.BINOP;
import tree.CALL;
import tree.CJUMP;
import tree.CONST;
import tree.ESEQ;
import tree.EVAL;
import tree.Exp;
import tree.ExpList;
import tree.JUMP;
import tree.LABEL;
import tree.MEM;
import tree.MOVE;
import tree.NAME;
import tree.NameOfLabel;
import tree.SEQ;
import tree.Stm;
import tree.TEMP;
import tree.TreePrint;

public class Translate implements SyntaxTreeVisitor <LazyIRTree>{
	/*
	 * Visits each node in the AST and constructs an IR tree and stack frame
	 * for each method declaration.
	 * 
	 * The IR tree and frame are paired and accumulated in a list of procedure fragments
	 * 
	 */
	
	private static final int WORD_SIZE = 4;
	
	ArrayList <ProcFrag> procedureFragments;
	HashMap <String, ClassEntry> symbolTable;
	String currentClass;
	NAME currentFrame;
	
	public Translate (HashMap<String, ClassEntry> table) {
		procedureFragments = new ArrayList <ProcFrag> ();
		symbolTable = table;
	}

	public ArrayList<ProcFrag> getFragments () {
		return procedureFragments;
	}
	
	public SEQ arrayToSeq (Stm[] statements) {
		int length = statements.length;
		//get the last two items
		SEQ s;
		if(length >= 2) {
			s = new SEQ (statements[length - 2], statements [length - 1]);
			if(length >= 3) {
				for(int i = length - 3; i >= 0; i--) {
					s = new SEQ(statements[i], s);
				}
			}
		}
		else {
			s = new SEQ (statements[0], null);
		}
		return s;
	}
	
	public ExpList convertToExpList (List<Expression> exps) {
		ExpList expressions;
		if(exps.size() >= 2) {
			LazyIRTree lastExp = exps.get(exps.size() -1).accept(this);
			expressions = new ExpList(lastExp.asExp(), null);
			for(int i = exps.size() - 2; i >= 0; i--) {
				LazyIRTree currentExp = exps.get(i).accept(this);
				ExpList front = new ExpList (currentExp.asExp(), expressions);
				expressions = front;
			}
		}
		else {
			LazyIRTree onlyExp = exps.get(0).accept(this);
			expressions = new ExpList(onlyExp.asExp(), null);
		}
		return expressions;
	}
	
	@Override
	public LazyIRTree visit(Program n) {
		n.m.accept(this);
		for(ClassDecl cd: n.cl) {
			cd.accept(this);
		}
		
		/*
		for(int i = 0; i < procedureFragments.size(); i++) {
			System.out.println(TreePrint.toString(procedureFragments.get(i).body));
		}
		*/
		return null;
	}

	@Override
	public LazyIRTree visit(MainClass n) {
		LazyIRTree main = n.s.accept(this);
		SEQ stms = SEQ.fromList(
				//new LABEL(n.i1.s + "$main$0"),
				new LABEL(n.i1.s +"$main$preludeEnd"),
				main.asStm(),
				new JUMP(n.i1.s + "$main$epilogBegin")
				);
		procedureFragments.add(new ProcFrag(stms, n.i1.s + "$main"));
		return null;
	}

	@Override
	public LazyIRTree visit(SimpleClassDecl n) {
		currentClass = n.i.s;
		for(MethodDecl method: n.ml) {
			procedureFragments.add(new ProcFrag(method.accept(this).asStm(), currentClass + "$" + method.i.s));
		}
		return null;
	}

	@Override
	public LazyIRTree visit(ExtendingClassDecl n) {
		currentClass = n.i.s;
		for(MethodDecl method: n.ml) {
			procedureFragments.add(new ProcFrag(method.accept(this).asStm(), currentClass));
		}
		return null;
	}

	@Override
	public LazyIRTree visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LazyIRTree visit(MethodDecl n) {
		ClassEntry className = symbolTable.get(currentClass);
		className.setCurrentMethod(n.i.s);
		ArrayList<Stm> statements = new ArrayList<Stm>();
		//statements.add(new LABEL(className.getClassName() + "$" + n.i.s + "$" + procedureFragments.size()));
		//statements.add(LABEL.generateLABEL(className.getClassName() + "$" + n.i.s));
		statements.add(new LABEL(className.getClassName() + "$" + n.i.s + "$preludeEnd"));
		for(Statement s: n.sl) {
			LazyIRTree current = s.accept(this);
			statements.add(current.asStm());
		}
		LazyIRTree returnExpression = n.e.accept(this);
		statements.add(returnExpression.asStm());
		statements.add(new JUMP (className.getClassName() + "$" + n.i.s + "$epilogBegin"));
		Stm[] states = new Stm[statements.size()];
		for(int i = 0; i < statements.size(); i++){
			states [i] = statements.get(i);
		}
		SEQ s = arrayToSeq(states);
		//System.out.println(TreePrint.toString(s));
		return new StatementIRTree(s);
	}

	@Override
	public LazyIRTree visit(Formal n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LazyIRTree visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LazyIRTree visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LazyIRTree visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LazyIRTree visit(IdentifierType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LazyIRTree visit(Block n) {
		ArrayList <Stm> statements = new ArrayList<Stm>();
		for(Statement s: n.sl) {
			LazyIRTree state = s.accept(this);
			statements.add(state.asStm());
		}
		Stm [] arrayStatements = (Stm[]) statements.toArray();
		return new StatementIRTree(SEQ.fromList(arrayStatements));
	}

	@Override
	public LazyIRTree visit(If n) {
		LazyIRTree conditional = n.e.accept(this);
		LazyIRTree thenStatements = n.s1.accept(this);
		LazyIRTree elseStatements = n.s2.accept(this);
		
		return new IfThenElseExp (conditional, thenStatements, elseStatements);
	}

	@Override
	public LazyIRTree visit(While n) {
		//Nx(SEQ(SEQ(c.unCx(b,x), SEQ(LABEL(b), s.unNx())), SEQ(c.unCx(b,x), LABEL(x)))
		LazyIRTree condition = n.e.accept(this);
		LazyIRTree stm = n.s.accept(this);
		LABEL statements = LABEL.generateLABEL("while", "loop");
		LABEL endWhile = LABEL.generateLABEL("while", "end");
		CJUMP conditionalCompare = new CJUMP (CJUMP.EQ, condition.asExp(), new CONST(1), statements.label, endWhile.label);
		SEQ sequence = SEQ.fromList(
				conditionalCompare,
				statements,
				stm.asStm(),
				endWhile
				);
		return new StatementIRTree(sequence);
	}

	@Override
	public LazyIRTree visit(Print n) {
		LazyIRTree printExpression = n.e.accept(this);
		return new StatementIRTree(new EVAL(new CALL(new NAME("println"), printExpression.asExp())));
	}

	@Override
	public LazyIRTree visit(Assign n) {
		//move the constant into the memory location of the variable name
		LazyIRTree expTree = n.e.accept(this);
		ClassEntry current = symbolTable.get(currentClass);
		MethodEntry currentMethod = current.getMethod(current.getCurrentMethod());
		int localNumber = currentMethod.getLocalNumber(n.i.s);
		if(localNumber != -1) {
			int locationNumber = (localNumber + 1) * WORD_SIZE;
			
			BINOP subtraction = new BINOP (BINOP.MINUS, new TEMP("%fp"), new CONST(locationNumber));
			MEM memMove = new MEM (subtraction);
			MOVE move = new MOVE (memMove, expTree.asExp());
			
			return new StatementIRTree(move);
		}
		return null;
	}

	@Override
	public LazyIRTree visit(ArrayAssign n) {
		//move the constant into the location in memory of that index of the array
		LazyIRTree arrayIndex = n.e1.accept(this);
		LazyIRTree assignmentValue = n.e2.accept(this);		

		ClassEntry current = symbolTable.get(currentClass);
		MethodEntry currentMethod = current.getMethod(current.getCurrentMethod());
		int localNumber = currentMethod.getLocalNumber(n.i.s);
		if(localNumber != -1) {
			int arrayStartingPosition = localNumber * WORD_SIZE;
			BINOP arrayIndexPosition = new BINOP (BINOP.PLUS, new CONST(arrayStartingPosition), new BINOP(BINOP.MUL, arrayIndex.asExp(), new CONST(WORD_SIZE)));
			MEM memLocation = new MEM (arrayIndexPosition);
			MOVE move = new MOVE(memLocation, assignmentValue.asExp());
			
			return new StatementIRTree(move);
		}
		return null;
	}

	@Override
	public LazyIRTree visit(And n) {
		LazyIRTree left = n.e1.accept(this);
		LazyIRTree right = n.e2.accept(this);
		
		return new AndIRTree (left, right);
	}

	@Override
	public LazyIRTree visit(LessThan n) {
		LazyIRTree left = n.e1.accept(this);
		LazyIRTree right = n.e2.accept(this);
		
		return new RelCondition(left.asExp(), right.asExp(), CJUMP.LT);
	}

	@Override
	public LazyIRTree visit(Plus n) {
		LazyIRTree left = n.e1.accept(this);
		LazyIRTree right = n.e2.accept(this);
		BINOP operation = new BINOP (BINOP.PLUS, left.asExp(), right.asExp());
		
		return new ExpressionIRTree(operation);
	}

	@Override
	public LazyIRTree visit(Minus n) {
		LazyIRTree left = n.e1.accept(this);
		LazyIRTree right = n.e2.accept(this);
		BINOP operation = new BINOP (BINOP.MINUS, left.asExp(), right.asExp());
	
		return new ExpressionIRTree(operation);
	}

	@Override
	public LazyIRTree visit(Times n) {
		LazyIRTree left = n.e1.accept(this);
		LazyIRTree right = n.e2.accept(this);
		BINOP operation = new BINOP (BINOP.MUL, left.asExp(), right.asExp());
		
		return new ExpressionIRTree(operation);
	}

	@Override
	public LazyIRTree visit(ArrayLookup n) {
		//Ex(MEM(ADD(e.unEx(), x(i.unEx(), CONST(w)))))
		LazyIRTree e = n.e1.accept(this);
		LazyIRTree i = n.e2.accept(this);
		BINOP mul = new BINOP(BINOP.MUL, 
				new BINOP (BINOP.PLUS, i.asExp(), new CONST(1))
					, new CONST(WORD_SIZE));
		BINOP add = new BINOP(BINOP.AND, e.asExp(), mul);
		return new ExpressionIRTree(new MEM(add));
	}

	@Override
	public LazyIRTree visit(ArrayLength n) {
		LazyIRTree arrayName = n.e.accept(this);
		return new StatementIRTree(LABEL.generateLABEL("array length here"));
	}

	@Override
	public LazyIRTree visit(Call n) {
		LazyIRTree expDetails = n.e.accept(this);
		String firstName = "";
		if(expDetails instanceof ExpressionIRTree) {
			ExpressionIRTree expressionTree = (ExpressionIRTree) expDetails;
			if(expressionTree.exp instanceof NAME) {
				firstName = ((NAME) expressionTree.exp).label.toString();
				NAME callName = new NAME(firstName + "$" + n.i.s);
				ExpList parameters = convertToExpList(n.el);
				return new ExpressionIRTree(new CALL(callName, parameters));
			}
			else if (expressionTree.exp instanceof CALL) {
				String className = null;
				for(String key: symbolTable.keySet()) {
					ClassEntry currentClass = symbolTable.get(key);
					if(currentClass.getMethodContained(n.i.s)) {
						className = key;
						break;
					}
				}
				if (className != null) {
					NAME callName = new NAME(className + "$" + n.i.s);
					ExpList parameters = convertToExpList(n.el);
					return new ExpressionIRTree(new CALL(callName, parameters));
				}
			}
		}
		NAME callName = new NAME(n.i.s);
		ExpList parameters = convertToExpList(n.el);
		ExpList fullParameters = new ExpList (expDetails.asExp(), parameters);
		/*
		SEQ sequenceOfMoves = SEQ.fromList(
				expMove,
				new MOVE(objectCallTemp,
						new CALL(callName, fullParameters))
				);
		return new StatementIRTree(sequenceOfMoves);
		*/
		return new ExpressionIRTree (new CALL(callName, fullParameters));
	}

	@Override
	public LazyIRTree visit(IntegerLiteral n) {
		return new ExpressionIRTree(new CONST(n.i));
	}

	@Override
	public LazyIRTree visit(True n) {
		return new ExpressionIRTree(new CONST(1));
	}

	@Override
	public LazyIRTree visit(False n) {
		return new ExpressionIRTree(new CONST(0));
	}

	@Override
	public LazyIRTree visit(IdentifierExp n) {
		ClassEntry currentClassEntry = symbolTable.get(currentClass);
		MethodEntry currentMethod = currentClassEntry.getMethod(currentClassEntry.getCurrentMethod());
		TEMP returnTemp = null;
		if(currentMethod.getParamContained(n.s)) {
			//if the identifier is a parameter
			int paramNumber = currentMethod.getParamNumber(n.s);
			if(paramNumber != -1) {
				returnTemp = new TEMP("%i" + (paramNumber+1));
			}
		}
		else if (currentMethod.getLocalContained(n.s)){
			//the identifier is a local
			int localNumber = currentMethod.getLocalNumber(n.s);
			if(localNumber != -1) {
				int addressOffset = localNumber + WORD_SIZE;
				MEM variableLocation = new MEM (new BINOP(BINOP.MINUS, new TEMP("%fp"), new CONST(addressOffset)));
				MOVE storeResult = new MOVE(new TEMP("%0"), variableLocation);
				return new StatementIRTree(storeResult);
			}
		}
		else {
			return new ExpressionIRTree(new NAME(NameOfLabel.generateLabel(n.s)));
		}
		return new ExpressionIRTree(returnTemp);
	}

	@Override
	public LazyIRTree visit(This n) {
		ClassEntry current = symbolTable.get(currentClass);
		return new ExpressionIRTree(new NAME(current.getClassName()));
	}

	@Override
	public LazyIRTree visit(NewArray n) {
		LazyIRTree arraySize = n.e.accept(this);
		TEMP arrayHolder = TEMP.generateTEMP("array_size_holder");
		MOVE mov = new MOVE (arrayHolder, arraySize.asExp());
		NAME newName = new NAME ("new_array");
		BINOP mul = new BINOP (BINOP.MUL, TEMP.generateTEMP(), new CONST(WORD_SIZE));
		ExpList expList = ExpList.toExpList(mul, TEMP.generateTEMP());
		CALL call = new CALL(newName, expList);
		ESEQ esequence = new ESEQ(
				mov,
				call
				);
		return new ExpressionIRTree(esequence);
	}

	@Override
	public LazyIRTree visit(NewObject n) {
		ClassEntry classObject = symbolTable.get(n.i.s);
		int childFields = classObject.getFieldCount();
		CALL allocationCall;
		if(classObject.extendsParentClass()) {
			ClassEntry parentClass = symbolTable.get(classObject.getParentClass());
			int parentFields = parentClass.getFieldCount();
			int totalFields = parentFields + childFields;
			
			allocationCall = new CALL(new NAME("_alloc_object"), new CONST(totalFields));
		}
		else {
			allocationCall = new CALL(new NAME("_alloc_object"), new CONST(childFields));
		}
		return new ExpressionIRTree(allocationCall);
	}

	@Override
	public LazyIRTree visit(Not n) {
		LazyIRTree exp = n.e.accept(this);
		BINOP opposite = new BINOP (BINOP.XOR, exp.asExp(), new CONST(1));

		return new ExpressionIRTree(opposite);
	}

	@Override
	public LazyIRTree visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}

}
