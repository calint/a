package a.pz.bas;

import java.io.IOException;
import a.pz.bas.assembly.inc;
import b.xwriter;

final public class expr_increment extends expr{
	public expr_increment(final program p,final String register) throws IOException{
		super(p,register);
		txt=new xwriter().p(register).p("++").toString();
	}
	@Override public void compile(program p){
		final instr s=new instr(p,0,inc.op,null,to_register);
		s.compile(p);
		bin=s.bin;
	}
	private static final long serialVersionUID=1;
}