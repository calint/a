package a.pzm.lang;

import a.pzm.lang.reader.token;

final public class call_inc extends call{
	final public static int op=0x200;
	public call_inc(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(1);
		final expression rd=arguments.get(0);
		if(rd.has_annotation("const"))throw new compiler_error(rd,"cannot write to const '"+rd.toString()+"'","declared at: ");
		x.vspc().assert_var_writable(rd,rd.token.name,null);
		final int rdi=x.vspc().get_register_index(rd,rd.token.name);
		x.write_op(this,op,0,rdi);
	}
	private static final long serialVersionUID=1;
}