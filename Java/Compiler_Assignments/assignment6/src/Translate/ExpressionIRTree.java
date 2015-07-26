package translate;

import tree.Exp;
import tree.LABEL;
import tree.Stm;

public class ExpressionIRTree extends LazyIRTree{
	Exp exp;
	
	public ExpressionIRTree(Exp e) {
		exp = e;
	}
	
	@Override
	Exp asExp() {
		return exp;
	}

	@Override
	Stm asStm() {
		return new tree.EVAL(exp);
	}

	@Override
	Stm asCond(LABEL t, LABEL f) {
		// TODO Auto-generated method stub
		return null;
	}

}
