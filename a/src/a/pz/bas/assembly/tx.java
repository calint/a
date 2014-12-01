package a.pz.bas.assembly;

import java.io.IOException;
import a.pz.bas.instr;
import a.pz.bas.program;

final public class tx extends instr{
	final public static int op=0x00e0;
	public tx(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
	}
	private static final long serialVersionUID=1;
}