package a.pzm.bas.assembly;

import java.io.IOException;
import a.pzm.bas.instr;
import a.pzm.bas.program;
import b.xwriter;

final public class stc extends instr{
	final public static int op=0x0040;
	public stc(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line());
		txt=new xwriter().p("stc").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}