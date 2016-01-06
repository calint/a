package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_neg extends call{
	private static final long serialVersionUID=1;
	public call_neg(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(pt,nm,b,annotations,"neg",r);
	}
	@Override public void binary_to(xbin x){
		final int rdi=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int zni=apply_znxr_annotations_on_instruction(0x0300|(rdi&63)<<14);
		//? inc reg imm4
		x.write(zni);
	}
}