package translate;

import tree.CJUMP;
import tree.CONST;
import tree.ESEQ;
import tree.EVAL;
import tree.Exp;
import tree.LABEL;
import tree.MOVE;
import tree.NameOfLabel;
import tree.SEQ;
import tree.Stm;
import tree.TEMP;

public class AndIRTree extends LazyIRTree{
	LazyIRTree left, right;
	
	public AndIRTree (LazyIRTree l, LazyIRTree r) {
		left = l;
		right = r;
	}

	@Override
	Exp asExp() {
		TEMP t = TEMP.generateTEMP();
		LABEL t1 = new LABEL (NameOfLabel.generateLabel("and", "true", "1"));
		LABEL t2 = new LABEL (NameOfLabel.generateLabel("and", "true", "2"));
		LABEL f = new LABEL (NameOfLabel.generateLabel("and", "false"));
		SEQ s = SEQ.fromList(
				new CJUMP (CJUMP.EQ, left.asExp(), new CONST(1), t1.label, f.label),
				t1,
				new CJUMP (CJUMP.EQ, right.asExp(), new CONST(1), t2.label, f.label),
				t2,
				new MOVE (t, new CONST(1)),
				f,
				new MOVE (t, new CONST(0))
				);
		return new ESEQ(s, t);
	}

	@Override
	Stm asStm() {
		return new EVAL (asExp());
	}

	@Override
	Stm asCond(LABEL t, LABEL f) {
		LABEL firstTrue = new LABEL (NameOfLabel.generateLabel("and", "true"));
		SEQ s = SEQ.fromList(
				left.asCond(firstTrue, f),
				firstTrue,
				right.asCond(t, f)
				);		
		return s;
	}

}
