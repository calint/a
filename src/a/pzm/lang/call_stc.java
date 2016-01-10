package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

final public class call_stc extends call{
	final public static int op=0x40;
	private static final long serialVersionUID=1;
	public call_stc(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"stc",r);
	}
	@Override public void binary_to(xbin x){
		final expression era=arguments.get(0);
		final expression erd=arguments.get(1);
//		if(!x.is_register_alias_exists(era.token)){
//			throw new compiler_error(era,"var '"+era.token+"' not declared",x.register_aliases.keySet().toString());
//		}
		final ArrayList<String>allocated_reg=new ArrayList<>();
		final int rdi;
		if(!x.is_alias_declared(erd.token)){
			final String tempregstr=x.allocate_register(this);
			x.alias_register(tempregstr,tempregstr);
			allocated_reg.add(tempregstr);
			rdi=x.register_index_for_alias(this,tempregstr);
			x.write(0|0x0000|(rdi&63)<<14);//li(rai imm20)
			final int d=erd.eval(x);
			x.write(d);
		}else{
			rdi=x.register_index_for_alias(erd,erd.token);
		}
		final int rai=x.register_index_for_alias(era,era.token);
		final int i=op|(rai&63)<<8|(rdi&63)<<14;
		final int znxr_i=apply_znxr_annotations_on_instruction(i);
		x.write(znxr_i);
		allocated_reg.forEach(e->x.unalloc(this,e));
	}
}