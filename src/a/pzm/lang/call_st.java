package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

final public class call_st extends call{
	private static final long serialVersionUID=1;
	public call_st(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot,"st",r);
	}
	@Override public void binary_to(xbin x){
		final ArrayList<String>allocated_regs=new ArrayList<>();
		final expression ra=arguments.get(0);
		final int rai;
		if(x.vspc().is_declared(ra.token)){//alias
			rai=x.vspc().get_register_index(ra,ra.token);
		}else{// load
			rai=x.vspc().alloc_var(ra,"$ra");
			allocated_regs.add("$ra");
			x.write(0|0x00000|(rai&63)<<14,ra);//li(rai imm20)
			x.add_at_pre_link_evaluate(ra);
			x.write(0,ra);
		}


		final expression rd=arguments.get(1);
		final int rdi;
		if(x.vspc().is_declared(rd.token)){//alias
			rdi=x.vspc().get_register_index(rd,rd.token);
		}else{
			rdi=x.vspc().alloc_var(ra,"$rd");
			allocated_regs.add("$rd");
			x.write(0|0x00000|(rdi&63)<<14,rd);//li(rai imm20)
			final int d=rd.eval(x);
			x.write(d,rd);
		}
		final int i=0x00d8|(rai&63)<<8|(rdi&63)<<14;
		final int zni=apply_znxr_annotations_on_instruction(i);
		x.write(zni,this);
		allocated_regs.forEach(s->x.vspc().free_var(this,s));
	}
}