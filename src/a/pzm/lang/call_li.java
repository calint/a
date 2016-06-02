package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_li extends call{
	private static final long serialVersionUID=1;
	final public static int op=0;
	public call_li(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(b,annotations,"li",r);
	}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		x.write_op(this,op,0,rdi);
		final expression imm=arguments.get(1);
		x.at_link_eval(imm);
		x.write(0,imm);
	}
}