package a.pz;

import java.io.IOException;

final public class nxt extends instr{
	final public static int op=0x0004;
	public nxt(program r) throws IOException{
		super(r,0,nxt.op,null,null);
		txt="nxt";
	}
	private static final long serialVersionUID=1;
}