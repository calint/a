package a.pzm.lang;

import java.util.LinkedHashMap;

import a.pzm.core;

final public class call_addi extends call{
	final public static int op=0x18;
	public call_addi(statement parent,LinkedHashMap<String,String>annotations,reader r){super(parent,annotations,"addi",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rai=x.vspc().get_register_index(this,arguments.get(0).token);
		final int rdi=Integer.parseInt(arguments.get(1).token);
		if(rdi>core.reg_imm_max||rdi<core.reg_imm_min)throw new compiler_error(this,"immediate value '"+arguments.get(1).token+"' out of range ["+core.reg_imm_min+" .. "+core.reg_imm_max+"]","");
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}