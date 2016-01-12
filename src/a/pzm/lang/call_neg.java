package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_neg extends call{
	private static final long serialVersionUID=1;
	final public static int op=0x300;
	public call_neg(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(b,annotations,"neg",r);
	}
	@Override public void binary_to(xbin x){
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		final int zni=apply_znxr_annotations_on_instruction(op|(rdi&63)<<14);
		//? inc reg imm4
//		x.write(zni,this);
		x.write_op(this,op,0,rdi);
	}
}