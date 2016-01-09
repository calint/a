package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_shf extends call{
	private static final long serialVersionUID=1;
	public call_shf(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"shf",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=declared_register_index_from_string(x,this,arguments.get(0).token);
		final expression rd=arguments.get(1);
		final int im4=rd.eval(x);
		//?
		if(im4<-8||im4>7) throw new compiler_error(this,"shift range between -8 and 7",""+im4);//? -8 8  shf a 0 being a>>1 
		final int i=0x0060|(im4&63)<<8|(rai&63)<<14;
		x.write(apply_znxr_annotations_on_instruction(i));
	}
}