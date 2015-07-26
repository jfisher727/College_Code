package translate;

import tree.Exp;
import tree.LABEL;
import tree.Stm;
import tree.TEMP;

abstract class ConditionIRTree extends LazyIRTree{
		
	Exp asExpression () {
		TEMP r = new TEMP("");
		LABEL t = new LABEL ();
		LABEL f = new LABEL ();
		
		return new tree.ESEQ(
				new tree.SEQ(new tree.MOVE(new tree.TEMP(r.toString()), 
											new tree.CONST(1)),
						new tree.SEQ(asCond(t, f),
								new tree.SEQ(new tree.LABEL(f.toString()),
										new tree.SEQ(new tree.MOVE(new tree.TEMP(r.toString()), 
																	new tree.CONST(0)),
										new tree.LABEL(t.toString()))))),
									new tree.TEMP(r.toString()));
	}	
	//as statement left as exercise
}
