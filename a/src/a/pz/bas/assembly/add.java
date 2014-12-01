package a.pz.bas.assembly;

import java.io.IOException;
import a.pz.bas.instr;
import a.pz.bas.program;
import b.xwriter;

final public class add extends instr{
	final public static int op=0x00a0;
	public add(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line());
		txt=new xwriter().p("add").spc().p(ra).spc().p(rd).toString();
	}
	private static final long serialVersionUID=1;
}