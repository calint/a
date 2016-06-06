package a.pzm.lang;

import java.util.LinkedHashMap;

import a.pzm.core;

final public class call_shf extends call{
	final public static int op=0x60;
	public call_shf(statement parent,LinkedHashMap<String,String>annot,reader r){super(parent,annot,"shf",r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final int rdi=x.vspc().get_register_index(this,arguments.get(0).token);
		final expression rd=arguments.get(1);
		final int imm6=rd.eval(x);
		if(imm6==0)throw new compiler_error(this,"shift 0 is not allowed",null);
		if(imm6<core.reg_imm_min||imm6>core.reg_imm_max)throw new compiler_error(this,"shift '"+imm6+"' is out of range "+core.reg_imm_min+" and "+core.reg_imm_max+"",null);//? -8 8  shf a 0 being a>>1 
		x.write_op(this,op,imm6,rdi);
	}
	private static final long serialVersionUID=1;
}