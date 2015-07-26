package translate;

import tree.Stm;

public class ProcFrag extends Fragment{
	public Stm body; //object that represents the method body as an IR tree
	
	public ProcFrag (Stm b) {
		body = b;
	}
}
