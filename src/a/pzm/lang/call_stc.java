package a.pzm.lang;

import a.pzm.lang.reader.token;

final public class call_stc extends call{
	final public static int op=0x40;
	public call_stc(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final expression ra=arguments.get(0);
		x.vspc().assert_var_writable(this,ra.token.name,null);
		final expression rd=arguments.get(1);
		final int rai=x.vspc().get_register_index(ra,ra.token.name);
		final int rdi=x.vspc().get_register_index(rd,rd.token.name);
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}