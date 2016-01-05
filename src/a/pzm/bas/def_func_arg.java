package a.pzm.bas;

import java.io.IOException;
import b.xwriter;

final public class def_func_arg extends def{
	protected String default_value;
	protected boolean is_const;
	public def_func_arg(program p) throws IOException{
		super(p);
		type=p.next_token_in_line();
		if(type.equals("const")){
			is_const=true;
			type=p.next_token_in_line();
		}
		name=p.next_token_in_line();
		if(!p.is_next_char_equals())
			throw new compiler_error(this,"expected function argument format: int a=2,...");
		default_value=p.next_token_in_line();
		final xwriter x=new xwriter();
		if(is_const)
			x.p("const").spc();
		x.p(type).spc().p(name).p("=").p(default_value);
		txt=x.toString();
	}
	private static final long serialVersionUID=1;
}