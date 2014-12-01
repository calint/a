package a.pz.bas;

import java.io.IOException;
import b.xwriter;

final public class data_int extends stmt{
	public String name,type,default_value;
	public data_int(String name,String type,program p) throws IOException{
		super(p);
		if(p.is_next_char_equals())//default value
			default_value=p.next_token_in_line();
		final xwriter x=new xwriter().p(type).spc().p(name);
		if(default_value!=null)
			x.p("=").p(default_value);
		txt=x.toString();
		//			r.skip_whitespace_on_same_line();
		if(!p.is_next_char_end_of_line())
			throw new compiler_error(this,"expected end of line after: ["+txt+"]");
	}
	@Override public void compile(program p){
		final int d=Integer.parseInt(default_value==null?"0":default_value,16);
		bin=new int[]{d};
	}
	private static final long serialVersionUID=1;
}