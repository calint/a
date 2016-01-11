package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_and extends call{
	private static final long serialVersionUID=1;
	final public static int op=0x58;
	public call_and(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"and",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=x.vspc().get_register_index(this,arguments.get(0).token);
		final int rdi=x.vspc().get_register_index(this,arguments.get(1).token);
		final int i=op|(rai&63)<<8|(rdi&63)<<14;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni,this);
	}
}