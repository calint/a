package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_neg extends call{
	final public static int op=0x300;
	public call_neg(statement parent,LinkedHashMap<String,String>annotations,reader r){super(parent,annotations,"neg",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(1);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		x.write_op(this,op,0,rdi);
	}
	private static final long serialVersionUID=1;
}