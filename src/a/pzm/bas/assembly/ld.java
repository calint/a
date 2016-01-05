package a.pzm.bas.assembly;

import java.io.IOException;
import a.pzm.bas.instr;
import a.pzm.bas.program;

final public class ld extends instr{
	final public static int op=0x00f8;
	public ld(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
	}
	private static final long serialVersionUID=1;
}