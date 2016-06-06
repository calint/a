package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_ldc extends call{
	final public static int op=0xc0;
	public call_ldc(statement parent,LinkedHashMap<String,String>annot,reader r){super(parent,annot,"ldc",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rai=x.vspc().get_register_index(this,arguments.get(1).token);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}