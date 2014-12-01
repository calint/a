package a.pz;

import java.io.IOException;

final public class ld extends instr{
	final public static int op=0x00f8;
	public ld(program r) throws IOException{
		super(r,0,op,r.next_token_in_line(),r.next_token_in_line(),true);
	}
	private static final long serialVersionUID=1;
}