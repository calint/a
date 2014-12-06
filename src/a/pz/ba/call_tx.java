package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_tx extends call{
	private static final long serialVersionUID=1;
	public call_tx(a pt,String nm,LinkedHashMap<String,String> annotations,reader r){
		super(pt,nm,annotations,"tx",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression ra=arguments.get(1);
		if(ra.src.length()!=1) throw new Error("not a register: "+ra.src);
		final int rai=ra.src.charAt(0)-'a';
		if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
		final expression rd=arguments.get(0);
		if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
		final int rdi=rd.src.charAt(0)-'a';
		if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
		final int i=0x00e0|(rai&15)<<8|(rdi&15)<<12;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
}