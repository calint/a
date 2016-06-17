package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.a;

final public class call_li extends call{
	final public static int op=0;
	public call_li(a pt,annotations annot,token tkn,reader r,statement b){super(b,annot,tkn,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final expression ra=arguments.get(0);
		x.vspc().assert_var_writable(this,ra.token.name,null);
		final int rdi=x.vspc().get_register_index(this,ra.token.name);
		x.write_op(this,op,0,rdi);
		final expression imm=arguments.get(1);
		x.at_link_eval(imm);
		x.write(0,imm);
	}
	private static final long serialVersionUID=1;
}