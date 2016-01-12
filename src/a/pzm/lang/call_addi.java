package a.pzm.lang;

import java.util.LinkedHashMap;

final public class call_addi extends call{
	private static final long serialVersionUID=1;
	final public static int op=0x18;
	public call_addi(statement parent,LinkedHashMap<String,String>annotations,reader r){
		super(parent,annotations,"addi",r);
	}
	@Override public void binary_to(xbin x){
		final int rai=x.vspc().get_register_index(this,arguments.get(0).token);
		final int rdi=Integer.parseInt(arguments.get(1).token);
		if(rdi>31||rdi<-32)
			throw new compiler_error(this,"immediate value '"+arguments.get(1).token+"' out of range -32 .. 31","");
//		final int i=op|(rai&63)<<8|(rdi&63)<<14;
//		final int zni=apply_znxr_annotations_on_instruction(i);
//		x.write(zni,this);
		
		x.write_op(this,op,rai,rdi);
	}
}