package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_inc extends call{
	private static final long serialVersionUID=1;
	public call_inc(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,block b){
		super(pt,nm,annotations,"inc",r,b);
	}
	@Override public void binary_to(xbin x){
		final int rdi=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int zni=apply_znxr_annotations_on_instruction(0x0200|(rdi&15)<<12);
		//? inc reg imm4
		x.write(zni);
	}
}