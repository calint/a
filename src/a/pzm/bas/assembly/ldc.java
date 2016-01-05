package a.pzm.bas.assembly;

import java.io.IOException;
import a.pzm.bas.instr;
import a.pzm.bas.program;
import b.xwriter;

final public class ldc extends instr{
	final public static int op=0x00c0;
	public ldc(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
		txt=new xwriter().p("ldc").spc().p((char)(rdi+'a')).spc().p((char)(rai+'a')).toString();
	}
	private static final long serialVersionUID=1;
}