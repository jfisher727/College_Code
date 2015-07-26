package translate;

import tree.BINOP;
import tree.CJUMP;
import tree.ESEQ;
import tree.Exp;
import tree.LABEL;
import tree.NameOfLabel;
import tree.Stm;

public class RelCondition extends ConditionIRTree{
	private Exp left, right;
	private int comparisonOperator;
	
	public RelCondition (Exp l, Exp r, int operator) {
		left = l;
		right = r;
		comparisonOperator = operator;
	}

	@Override
	Stm asStm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Stm asCond(LABEL t, LABEL f) {
		return new CJUMP (comparisonOperator, left, right, t.label, f.label);
	}

	@Override
	Exp asExp() {
		//return super.asExpression();
		return null;
	}

}
