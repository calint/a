package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;

final public class call_st extends call{
	private static final long serialVersionUID=1;
	public call_st(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,statement b){
		super(pt,nm,b,annotations,"st",r);
	}
	@Override public void binary_to(xbin x){
		final expression arg1=arguments.get(0);
		final expression arg2=arguments.get(1);
		final int rai;
		final ArrayList<String>allocated_reg=new ArrayList<>();
		if(x.is_register_alias_exists(arg1.token)){
			rai=declared_register_index_from_string(x,this,arguments.get(0).token);
		}else{
			final String tempregstr=x.allocate_register(this);
			x.alias_register(tempregstr,tempregstr);
			allocated_reg.add(tempregstr);
			rai=x.register_index_for_alias(this,tempregstr);
			x.write(0|0x0000|(rai&63)<<14);//li(rai imm20)
			final int d=arg1.eval(x);
			x.write(d);
		}
		final int rdi;
		if(x.is_register_alias_exists(arg2.token)){
			rdi=declared_register_index_from_string(x,this,arguments.get(1).token);
		}else{
			final String reg=x.allocate_register(this);
			x.alias_register(reg,reg);
			allocated_reg.add(reg);
			rdi=x.register_index_for_alias(this,reg);
			x.write(0|0x0000|(rdi&63)<<14);//li(rai imm20)
			final int d=arg2.eval(x);
			x.write(d);
		}
		final int i=0x00d8|(rai&63)<<8|(rdi&63)<<14;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni);
		allocated_reg.forEach(e->x.unalloc(this,e));
	}
}