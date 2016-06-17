package a.pzm.lang;

import a.pzm.core;
import a.pzm.lang.reader.token;

final public class call_addi extends call{
	final public static int op=0x18;
	public call_addi(statement parent,annotations annot,token tk,reader r){super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		x.vspc().assert_var_writable(this,arguments.get(0).token.name,null);
		final int rai=x.vspc().get_register_index(this,arguments.get(0).token.name);
		final int rdi=Integer.parseInt(arguments.get(1).token.name);
		if(rdi>core.reg_imm_max||rdi<core.reg_imm_min)throw new compiler_error(this,"immediate value '"+arguments.get(1).token+"' is out of range ["+core.reg_imm_min+" .. "+core.reg_imm_max+"]","");
		x.write_op(this,op,rai,rdi);
	}
	private static final long serialVersionUID=1;
}