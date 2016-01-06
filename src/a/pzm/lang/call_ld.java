package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_ld extends call{
	private static final long serialVersionUID=1;
	public call_ld(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(pt,nm,b,annotations,"ld",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=declared_register_index_from_string(x,this,arguments.get(1).token);
		final int rdi=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int i=0x00f8|(rai&63)<<8|(rdi&63)<<14;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
}