package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_ldd extends call{
	private static final long serialVersionUID=1;
	final public static int op=0xb8;
	public call_ldd(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(b,annotations,"ldd",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=x.vspc().get_register_index(this,arguments.get(1).token);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		final int i=op|(rai&63)<<8|(rdi&63)<<14;
		x.write(apply_znxr_annotations_on_instruction(i),this);
	}
}