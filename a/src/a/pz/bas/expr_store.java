package a.pz.bas;

import java.io.IOException;
import a.pz.bas.assembly.st;
import a.pz.bas.assembly.stc;
import b.xwriter;

final public class expr_store extends expr{
	public String rhs;
	public boolean inca;
	public expr_store(final program p) throws IOException{
		super(p,p.next_token_in_line());
		if(p.is_next_char_plus()){
			if(!p.is_next_char_plus())
				throw new compiler_error(this,"expected format *a++=d");
			inca=true;
			p.skip_whitespace_on_same_line();
			if(!p.is_next_char_equals())
				throw new compiler_error(this,"expected format *a++=d");
			rhs=p.next_token_in_line();
			txt=new xwriter().p("*").p(to_register).p("++=").p(rhs).toString();
			return;
		}
		if(!p.is_next_char_equals())
			throw new compiler_error(this,"expected '='");
		rhs=p.next_token_in_line();
		txt=new xwriter().p("*").p(to_register).p("=").p(rhs).toString();
	}
	@Override public void compile(program p){
		//? ensure lhs,rhs are registers
		//			final expr lhse=expr.make_from_source_text(p,register);
		final instr s=new instr(p,0,inca?stc.op:st.op,to_register,rhs);
		s.compile(p);
		bin=s.bin;
	}
	private static final long serialVersionUID=1;
}