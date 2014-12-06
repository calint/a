package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_st extends call{
	private static final long serialVersionUID=1;
	public call_st(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,block b){
		super(pt,nm,annotations,"st",r,b);
	}
	@Override public void binary_to(xbin x){
		final int rai=register_index_from_string(this,arguments.get(0).token);
		final int rdi=register_index_from_string(this,arguments.get(1).token);
		final int i=0x00d8|(rai&15)<<8|(rdi&15)<<12;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni);
	}
}