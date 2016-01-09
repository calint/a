package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;

final public class call_addi extends call{
	private static final long serialVersionUID=1;
	public call_addi(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(b,annotations,"addi",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final int rai=declared_register_index_from_string(x,this,arguments.get(0).token);
		final int rdi=Integer.parseInt(arguments.get(1).token);
		if(rdi>31||rdi<-32)
			throw new compiler_error(this,"immediate value '"+arguments.get(1).token+"' out of range -32 .. 31","");
		final int i=0x18|(rai&63)<<8|(rdi&63)<<14;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni);
	}
}