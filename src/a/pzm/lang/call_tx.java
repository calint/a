package a.pzm.lang;

import java.util.LinkedHashMap;

import b.a;

final public class call_tx extends call{
	final public static int op=0xe0;
	public call_tx(a pt,String nm,LinkedHashMap<String,String>annotations,reader r,statement b){super(b,annotations,"tx",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rai=x.vspc().get_register_index(this,arguments.get(1).token);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}