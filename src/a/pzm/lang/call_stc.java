package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_stc extends call{
	final public static int op=0x40;
	private static final long serialVersionUID=1;
	public call_stc(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"stc",r);
	}
	@Override public void binary_to(xbin x){
		final expression ra=arguments.get(0);
		final expression rd=arguments.get(1);
		final int rai=x.vspc().get_register_index(ra,ra.token);
		final int rdi=x.vspc().get_register_index(rd,rd.token);
		final int i=op|(rai&63)<<8|(rdi&63)<<14;
		final int znxr_i=apply_znxr_annotations_on_instruction(i);
		x.write(znxr_i,this);
	}
}