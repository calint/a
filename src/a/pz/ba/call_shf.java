package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_shf extends call{
	public call_shf(a pt,String nm,LinkedHashMap<String,String>annotations,reader r){
		super(pt,nm,annotations,"shf",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression ra=arguments.get(0);
		if(ra.src.length()!=1) throw new Error("not a register: "+ra.src);
		final int rai=ra.src.charAt(0)-'a';
		if(rai<0||rai>15) throw new Error("source registers 'a' through 'p' available");
		final expression rd=arguments.get(1);
		final int im4=rd.eval(x);
		if(im4<-8||im4>7) throw new Error("shift range between -8 and 7");//? -8 8  shf a 0 being a>>1 
		final int i=0x0060|(im4&15)<<8|(rai&15)<<12;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
	private static final long serialVersionUID=1;
}