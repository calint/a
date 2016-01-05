package a.pzm.bas.assembly;

import java.io.IOException;
import a.pzm.bas.instr;
import a.pzm.bas.program;

final public class ret extends instr{
	final public static int op=0x0008;
	public ret(program r) throws IOException{
		super(r,0,ret.op,null,null);
		txt="ret";
	}
	private static final long serialVersionUID=1;
}