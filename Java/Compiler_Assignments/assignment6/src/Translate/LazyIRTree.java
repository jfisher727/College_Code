package translate;

import tree.LABEL;
import tree.Exp;
import tree.Stm;

abstract public class LazyIRTree {
	abstract Exp asExp();					//ESEQ (asStm(), CONST(0))
	abstract Stm asStm();					//EVAL (asExp())
	abstract Stm asCond (LABEL t, LABEL f);
}
