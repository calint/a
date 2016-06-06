package a.pzm.lang;

import a.pzm.lang.reader.token;

final public class call_add extends call{
	final public static int op=0xa0;
	public call_add(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rai=x.vspc().get_register_index(this,arguments.get(0).token.name);
		final int rdi=x.vspc().get_register_index(this,arguments.get(1).token.name);
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}