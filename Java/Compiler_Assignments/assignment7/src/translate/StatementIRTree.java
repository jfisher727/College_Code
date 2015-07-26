package translate;

import tree.Exp;
import tree.LABEL;
import tree.Stm;

public class StatementIRTree extends LazyIRTree {
	Stm stm;
	
	public StatementIRTree (Stm s) {
		stm = s;
	}

	//not implemented
	@Override
	Exp asExp() {
		return null;
	}

	@Override
	Stm asStm() {
		return stm;
	}

	//not implemented
	@Override
	Stm asCond(LABEL t, LABEL f) {
		// TODO Auto-generated method stub
		return null;
	}

}
