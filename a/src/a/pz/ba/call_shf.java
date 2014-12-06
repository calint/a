package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;

final public class call_shf extends call{
	private static final long serialVersionUID=1;
	public call_shf(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,block b){
		super(pt,nm,annotations,"shf",r,b);
	}
	@Override public void binary_to(xbin x){
		final int rai=register_index_from_string(this,arguments.get(0).token);
		final expression rd=arguments.get(1);
		final int im4=rd.eval(x);
		if(im4<-8||im4>7) throw new compiler_error(this,"shift range between -8 and 7",""+im4);//? -8 8  shf a 0 being a>>1 
		final int i=0x0060|(im4&15)<<8|(rai&15)<<12;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
}