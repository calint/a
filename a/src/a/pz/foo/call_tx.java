package a.pz.foo;

import java.util.LinkedHashMap;
import b.a;

final public class call_tx extends call{
	private static final long serialVersionUID=1;
	public call_tx(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,block b){
		super(pt,nm,annotations,"tx",r,b);
	}
	@Override public void binary_to(xbin x){
		final int rai=declared_register_index_from_string(x,this,arguments.get(1).token);
		final int rdi=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int i=0x00e0|(rai&15)<<8|(rdi&15)<<12;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
}