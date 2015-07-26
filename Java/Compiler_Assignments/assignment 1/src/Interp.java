import java.io.IOException;
import java.util.HashMap;



public class Interp {
	private static final int ID_EXP = 1;
	private static final int NUM_EXP = 2;
	private static final int OP_EXP = 3;
	private static final int ESEQ_EXP = 4;
	private static final int CMP_STM = 1;
	private static final int ASM_STM = 2;
	private static final int PRT_STM = 3;
	private static final int EL_PAIR = 1;
	private static final int EL_LAST = 2;
	
	
	static HashMap<String, Integer> map;
	
	public static int getExpressionInstance (Exp e) {
		int expressionType = 0;
		if(e instanceof IdExp) {
			expressionType = ID_EXP;
		}
		else if (e instanceof NumExp) {
			expressionType = NUM_EXP;
		}
		else if (e instanceof OpExp) {
			expressionType = OP_EXP;
		}
		else if (e instanceof EseqExp) {
			expressionType = ESEQ_EXP;
		}
		else {
			expressionType = -1;
		}
		return expressionType;
	}
	
	public static int getStatementInstance (Stm s) {
		int stmType = 0;
		if(s instanceof CompoundStm) {
			stmType = CMP_STM;
		}
		else if (s instanceof AssignStm) {
			stmType = ASM_STM;
		}
		else if (s instanceof PrintStm) {
			stmType = PRT_STM;
		}
		else {
			stmType = 0;
		}
		return stmType;
	}
	
	public static int getExpListInstance (ExpList el) {
		int listType = 0;
		if(el instanceof PairExpList) {
			listType = EL_PAIR;
		}
		else if (el instanceof LastExpList) {
			listType = EL_LAST;
		}
		else {
			listType = 0;
		}
		return listType;
	}
	
	public static int lookup (HashMap<String, Integer> h, String key) {
		int value = -1;
		if(h.containsKey(key)) {
			value = h.get(key);
		}
		return value;
	}

	public static void assign (HashMap<String, Integer> h, String key, int value) {
		if(h.containsKey(key)) {
			h.remove(key);
		}
		h.put(key, value);
	}	
	
	public static int opExpression (int operation, Exp left, Exp right) {
		int result = 0;
		int leftVal = 0, rightVal = 0;
		
		leftVal = expressionEvaluation (left);
		rightVal = expressionEvaluation (right);
		
		//perform the operation based on the operation code
		switch (operation) {
		case 1:
			result = leftVal + rightVal;
			break;
		case 2:
			result = leftVal - rightVal;
			break;
		case 3:
			result = leftVal * rightVal;
			break;
		case 4:
			result = leftVal / rightVal;
			break;
		default:
			result = 0;
			break;
		}
		
		//return the result of the operation
		return result;
	}
	
	public static int expressionEvaluation (Exp e) {
		int value = 0;
		switch(getExpressionInstance(e)) {
		case ID_EXP:
			IdExp id = (IdExp) e;
			value = lookup (map, id.id);
			break;
		case NUM_EXP:
			NumExp num = (NumExp) e;
			value = num.num;
			break;
		case OP_EXP:
			OpExp exp = (OpExp) e;
			value = opExpression (exp.oper, exp.left, exp.right);
			break;
		case ESEQ_EXP:
			EseqExp eseq = (EseqExp) e;
			interp (eseq.stm);
			value = expressionEvaluation(eseq.exp);
			break;
		default:
			value = 0;
			break;
		}
		
		return value;
	}

	public static void printExp (Exp e) {
		int expType = getExpressionInstance (e);
		switch (expType) {
		case ID_EXP:
			IdExp id = (IdExp) e;
			System.out.println (lookup(map, id.id));
			break;
		case NUM_EXP:
			NumExp num = (NumExp) e;
			System.out.println(num.num);
			break;
		case OP_EXP:
			OpExp op = (OpExp) e;
			System.out.println(opExpression(op.oper, op.left, op.right));
			break;
		case ESEQ_EXP:
			EseqExp eseq = (EseqExp) e;
			interp (eseq.stm);
			System.out.println(expressionEvaluation(eseq.exp));
			break;
		default:
			System.out.println();
			break;
		}
	}
	
	public static void printStm (ExpList e) {
		int type = getExpListInstance (e);
		if (type == EL_PAIR) {
			PairExpList temp = (PairExpList) e;
			printExp (temp.head);
			printStm (temp.tail);
		}
		else {
			LastExpList temp = (LastExpList) e;
			printExp(temp.head);
		}
	}	
	
	public static void interp (Stm s) {
		int statementType = getStatementInstance (s);
		switch (statementType) {
		case CMP_STM:
			CompoundStm compound = (CompoundStm) s;
			interp (compound.stm1);
			interp (compound.stm2);
			break;
		case ASM_STM:
			AssignStm assign = (AssignStm) s;
			assign(map, assign.id, expressionEvaluation(assign.exp));
			break;
		case PRT_STM:
			PrintStm print = (PrintStm) s;
			printStm(print.exps);
			break;
		default:
			break;
		}
	}
	
	public static int maxargs (Stm s) {
		int result = 0;
		int statementType = getStatementInstance(s);
		switch (statementType) {
		case CMP_STM:
			CompoundStm stm = (CompoundStm) s;
			int stm1 = maxargs(stm.stm1);
			int stm2 = maxargs(stm.stm2);
			if(stm1 > stm2) {
				result = stm1;
			}
			else {
				result = stm2;
			}
			break;
		case ASM_STM:
			AssignStm assign = (AssignStm) s;
			break;
		case PRT_STM:
			PrintStm printer = (PrintStm) s;
			int expressionType = getExpListInstance(printer.exps);
			if(expressionType == EL_PAIR) {
				PairExpList pairList = (PairExpList) printer.exps;
				result++;
				while(getExpListInstance(pairList.tail) == EL_PAIR){
					pairList = (PairExpList) pairList.tail;
					result++;
				}
				result++;
			}
			else {
				result++;
			}
			break;
		default:
			break;
		}
		return result;
	}
	
	public static void main (Stm s) throws IOException {
		System.out.println(maxargs(s));
		interp (s);
	}
	
	public static void main(String[] args) {
		map = new HashMap<String, Integer> ();
		
		try {			
			main(Example.a_program);
		map = new HashMap<String, Integer> ();
			main(Example.another_program);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
