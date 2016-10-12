package a.pzm.lang;

import a.pzm.lang.reader.token;

final public class call_ld extends call{
	final public static int op=0xf8;
	public call_ld(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		x.vspc().assert_var_writable(this,arguments.get(0).token.name,null);
		final int rai=x.vspc().get_register_index(this,arguments.get(1).token.name);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token.name);
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}