package translate;

import tree.CJUMP;
import tree.CONST;
import tree.EVAL;
import tree.Exp;
import tree.JUMP;
import tree.LABEL;
import tree.SEQ;
import tree.Stm;

public class IfThenElseExp extends LazyIRTree{	
	LazyIRTree conditional;
	Exp a, b;
	LABEL t = new LABEL("if", "then");
	LABEL f = new LABEL("if", "else");
	LABEL join = new LABEL("if", "join");
	
	public IfThenElseExp (LazyIRTree cc, Exp aa, Exp bb) {
		conditional = cc;
		a = aa;
		b = bb;
	}

	@Override
	Exp asExp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Stm asStm() {
		SEQ s = SEQ.fromList(
				conditional.asCond(t, f),
				t,
				new EVAL(a),
				new JUMP(join.label),
				f,
				new EVAL(b),
				join
				);
		return s;
	}

	@Override
	Stm asCond(LABEL tt, LABEL ff) {
		//tt and ff are the places to which conditions inside the then-clause (or else-clause)
		//must jump, depending on the truth of those subexpressions
		SEQ s = SEQ.fromList(
				conditional.asCond(t, f),
				t,
				new EVAL(a),
				new JUMP(join.label),
				f,
				new EVAL(b),
				join
				);
		return s;
	}

}
