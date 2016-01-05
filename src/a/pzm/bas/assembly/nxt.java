package a.pzm.bas.assembly;

import java.io.IOException;
import a.pzm.bas.instr;
import a.pzm.bas.program;

final public class nxt extends instr{
	final public static int op=0x0004;
	public nxt(program r) throws IOException{
		super(r,0,nxt.op,null,null);
		txt="nxt";
	}
	private static final long serialVersionUID=1;
}