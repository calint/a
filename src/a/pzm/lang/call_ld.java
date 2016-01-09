package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_ld extends call{
	private static final long serialVersionUID=1;
	public call_ld(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"ld",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=declared_register_index_from_string(x,this,arguments.get(1).token);
		final int rdi=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int i=0x00f8|(rai&63)<<8|(rdi&63)<<14;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
}