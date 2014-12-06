package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_li extends call{
	private static final long serialVersionUID=1;
	public call_li(a pt,String nm,LinkedHashMap<String,String> annotations,reader r){
		super(pt,nm,annotations,"li",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression rd=arguments.get(0);
		if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
		final int rdi=rd.src.charAt(0)-'a';
		int znxr=0;
		if(has_annotation("@ifp")) znxr|=3;
		if(has_annotation("@ifz")) znxr|=1;
		if(has_annotation("@ifn")) znxr|=2;
		if(has_annotation("@nxt")) znxr|=4;
		if(has_annotation("@ret")) znxr|=8;
		final int i=znxr|0x0000|0<<8|rdi<<12;
		x.write(i);
		final expression imm=arguments.get(1);
		x.at_pre_link_evaluate(imm);
		//			x.write(imm.eval(x));
		x.write(0);
	}
}