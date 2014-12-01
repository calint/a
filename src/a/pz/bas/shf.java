package a.pz.bas;

import java.io.IOException;

final public class shf extends instr{
	final public static int op=0x0060;
	public shf(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
	}
	private static final long serialVersionUID=1;
}