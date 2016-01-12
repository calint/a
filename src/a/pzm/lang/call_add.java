package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_add extends call{
	private static final long serialVersionUID=1;
	final public static int op=0xa0;
	public call_add(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"add",r);
	}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rai=x.vspc().get_register_index(this,arguments.get(0).token);
		final int rdi=x.vspc().get_register_index(this,arguments.get(1).token);
//		final int i=op|(rai&63)<<8|(rdi&63)<<14;
//		final int zni=apply_znxr_annotations_on_instruction(i);
//		x.write(zni,this);
		x.write_op(this,op,rai,rdi);
	}
}