package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_inc extends call{
	private static final long serialVersionUID=1;
	final public static int op=0x200;
	public call_inc(statement parent,LinkedHashMap<String,String>annotations,reader r){
		super(parent,annotations,"inc",r);
	}
	@Override public void binary_to(xbin x){
		ensure_arg_count(1);
		final expression rd=arguments.get(0);
		final int rdi=x.vspc().get_register_index(rd,rd.token);
//		final int zni=apply_znxr_annotations_on_instruction(op|(rdi&63)<<14);
//		x.write(zni,this);
		x.write_op(this,op,0,rdi);
	}
}