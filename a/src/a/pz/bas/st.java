package a.pz.bas;

import java.io.IOException;
import b.xwriter;

final public class st extends instr{
	final public static int op=0x00d8;
	public st(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line());
		txt=new xwriter().p("st").spc().p((char)(rai+'a')).spc().p((char)(rdi+'a')).toString();
	}
	private static final long serialVersionUID=1;
}