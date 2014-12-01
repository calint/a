package a.pz.bas.assembly;

import java.io.IOException;
import a.pz.bas.instr;
import a.pz.bas.program;
import b.xwriter;

final public class inc extends instr{
	final public static int op=0x0200;
	public inc(program r) throws IOException{
		super(r,0,op,null,r.next_token_in_line());
		txt=new xwriter().p("inc").spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}