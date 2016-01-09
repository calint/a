package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_add extends call{
	private static final long serialVersionUID=1;
	public call_add(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"add",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final int rai=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int rdi=declared_register_index_from_string(x,this,arguments.get(1).token);
		final int i=0x00a0|(rai&63)<<8|(rdi&63)<<14;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni);
	}
}