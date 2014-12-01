package a.pz;

import java.io.IOException;

final public class sub extends instr{
	final public static int op=0x0020;
	public sub(program r) throws IOException{
		super(r,0,sub.op,r.next_token_in_line(),r.next_token_in_line());
	}
	private static final long serialVersionUID=1;
}