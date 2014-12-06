package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_st extends call{
	private static final long serialVersionUID=1;
	public call_st(a pt,String nm,LinkedHashMap<String,String> annotations,reader r){
		super(pt,nm,annotations,"st",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression ra=arguments.get(0);
		if(ra.token.length()!=1) throw new Error("not a register: "+ra.token);
		final int rai=ra.token.charAt(0)-'a';
		if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
		final expression rd=arguments.get(1);
		if(rd.token.length()!=1) throw new Error("not a register: "+rd.token);
		final int rdi=rd.token.charAt(0)-'a';
		if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
		final int i=0x00d8|(rai&15)<<8|(rdi&15)<<12;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni);
	}
}