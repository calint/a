package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_not extends call{
	final public static int op=0x60;
	public call_not(statement parent,LinkedHashMap<String,String>annotations,reader r){super(parent,annotations,"not",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(1);
		final String rd=arguments.get(0).token;
		final int rdi=x.vspc().get_register_index(this,rd);
		x.write_op(this,op,0,rdi);
	}
	private static final long serialVersionUID=1;
}