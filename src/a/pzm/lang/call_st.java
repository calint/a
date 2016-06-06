package a.pzm.lang;

import java.util.ArrayList;

import a.pzm.lang.reader.token;

final public class call_st extends call{
	final public static int op=0xd8;
	public call_st(statement parent,annotations annot,token tk,reader r)throws Throwable{super(parent,annot,tk,r);}
	@Override public void binary_to(xbin x){
		ensure_arg_count(2);
		final ArrayList<String>allocated_regs=new ArrayList<>();
		final expression ra=arguments.get(0);
		final int rai;
		if(x.vspc().is_declared(ra.token.name)){//alias
			rai=x.vspc().get_register_index(ra,ra.token.name);
		}else{// load
			rai=x.vspc().alloc_var(ra,"$ra");
			allocated_regs.add("$ra");
			x.write_op(this,call_li.op,0,rai);
			x.at_link_eval(ra);
			x.write(0,ra);
		}
		final expression rd=arguments.get(1);
		final int rdi;
		if(x.vspc().is_declared(rd.token.name)){//alias
			rdi=x.vspc().get_register_index(rd,rd.token.name);
		}else{
			rdi=x.vspc().alloc_var(ra,"$rd");
			allocated_regs.add("$rd");
			x.write_op(this,call_li.op,0,rdi);
			final int d=rd.eval(x);
			x.write(d,rd);
		}
		x.write_op(this,op,rai,rdi);
		for(final String s:allocated_regs)
			x.vspc().free_var(this,s);
	}
	private static final long serialVersionUID=1;
}