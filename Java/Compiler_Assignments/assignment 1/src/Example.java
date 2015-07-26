
public class Example {	
	static Stm a_program = new CompoundStm(new AssignStm("a",new OpExp(new NumExp(5), OpExp.Plus, new NumExp(3))),
	        new CompoundStm(new AssignStm("b",
	                new EseqExp(
	                		new PrintStm(
	                				new PairExpList(
	                						new IdExp("a"),
	                						new LastExpList(
	                								new OpExp(
	                										new IdExp("a"), OpExp.Minus, new NumExp(1))
	                								)
	                						)
	                				),
	                      new OpExp(
	                    		  new NumExp(10), OpExp.Times, new IdExp("a")
	                    		  )
	                		)
	        ),
	                         new PrintStm(
	                        		 new LastExpList(
	                        				 new IdExp("b"))
	                        		 )
	        )
	);
	//stm :: a:= 5*5; 
	//out ::
	static Stm another_program =
			new CompoundStm(new AssignStm("a", new OpExp(new NumExp(5), OpExp.Times, new NumExp(5))),
					new CompoundStm(new AssignStm("b", new OpExp(new IdExp("a"), OpExp.Minus, new NumExp(10)))
					, new PrintStm(new PairExpList(new IdExp("a"), new LastExpList(new IdExp("b"))))));
}
