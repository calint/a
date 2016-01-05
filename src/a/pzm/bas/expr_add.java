package a.pzm.bas;

import java.io.IOException;
import a.pzm.bas.assembly.add;
import b.xwriter;

final public class expr_add extends expr{
	public String rhs;
	public expr_add(final program p,final String register) throws IOException{
		super(p,register);
		rhs=p.next_token_in_line();
		txt=new xwriter().p(register).p("+=").p(rhs).toString();
	}
	@Override public void compile(program p){
		final instr s=new instr(p,0,add.op,to_register,rhs);
		s.compile(p);
		bin=s.bin;
	}
	private static final long serialVersionUID=1;
}