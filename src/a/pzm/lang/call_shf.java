package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_shf extends call{
	private static final long serialVersionUID=1;
	final public static int op=0x60;
	public call_shf(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"shf",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=x.register_index_for_alias(this,arguments.get(0).token);
		final expression rd=arguments.get(1);
		final int imm6=rd.eval(x);
		if(imm6<-32||imm6>31) throw new compiler_error(this,"shift range between -32 and 31",""+imm6);//? -8 8  shf a 0 being a>>1 
		final int i=op|(imm6&63)<<8|(rai&63)<<14;
		x.write(apply_znxr_annotations_on_instruction(i),this);
	}
}