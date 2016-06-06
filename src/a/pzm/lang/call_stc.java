package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_stc extends call{
	final public static int op=0x40;
	public call_stc(statement parent,LinkedHashMap<String,String>annot,reader r){super(parent,annot,"stc",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final expression ra=arguments.get(0);
		final expression rd=arguments.get(1);
		final int rai=x.vspc().get_register_index(ra,ra.token);
		final int rdi=x.vspc().get_register_index(rd,rd.token);
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}