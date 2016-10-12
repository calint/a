package a.pzm.lang;

import a.pzm.lang.reader.token;

final public class call_neg extends call{
	final public static int op=0x300;
	public call_neg(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(1);
		x.vspc().assert_var_writable(this,arguments.get(0).token.name,null);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token.name);
		x.write_op(this,op,0,rdi);
	}
	private static final long serialVersionUID=1;
}