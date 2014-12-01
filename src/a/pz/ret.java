package a.pz;

import java.io.IOException;

final public class ret extends instr{
	final public static int op=0x0008;
	public ret(program r) throws IOException{
		super(r,0,ret.op,null,null);
		txt="ret";
	}
	private static final long serialVersionUID=1;
}