package a.pz.bas.assembly;

import java.io.IOException;
import a.pz.bas.instr;
import a.pz.bas.program;
import b.xwriter;

final public class lp extends instr{
	final public static int op=0x0100;
	public lp(program r) throws IOException{
		super(r,0,lp.op,null,r.next_token_in_line());
		txt=new xwriter().p("lp").spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}