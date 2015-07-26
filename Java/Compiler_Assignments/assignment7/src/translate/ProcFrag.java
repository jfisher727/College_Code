package translate;

import tree.Stm;

public class ProcFrag extends Fragment{
	public String fragmentName;
	public Stm body; //object that represents the method body as an IR tree
	
	public ProcFrag (Stm b, String name) {
		body = b;
		fragmentName = name;
	}
}
