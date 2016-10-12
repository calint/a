package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.xwriter;

public class expression_dereference extends expression{
	public expression_dereference(statement parent,annotations annot,reader r,String dest_reg){
		super(parent,annot,new token("","*",""),r,dest_reg);
		ws_leading=r.next_empty_space();
		expr_to_dereference=expression.read(parent,annot,dest_reg,r);
		mark_end_of_source(r);
		if(r.is_next_char_plus()){// maybe op plus    *ra+3
			if(r.is_next_char_plus()){// *ra++
				is_increment_pointer_after_dereference=true;
			}else{
				throw new compiler_error(this,"plus not implemented",null);
			}
		}else{
			is_increment_pointer_after_dereference=false;
		}
		ws_trailing=r.next_empty_space();
	}
	@Override public void binary_to(xbin x){
		if(destreg==null)throw new compiler_error(this,"expected destination register",null);
		x.vspc().assert_var_writable(this,destreg,null);
		expr_to_dereference.destreg=destreg;
		expr_to_dereference.binary_to(x);
		if(!is_increment_pointer_after_dereference){
			final int rai=x.vspc().get_register_index(this,expr_to_dereference.destreg);
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_ld.op,rai,rdi);
			return;
		}
		final int rai=x.vspc().get_register_index(this,expr_to_dereference.destreg);
		final int rdi=x.vspc().get_register_index(this,destreg);
		x.write_op(this,is_increment_pointer_after_dereference?call_ldc.op:call_ld.op,rai,rdi);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p(ws_leading);
		expr_to_dereference.source_to(x);
		if(is_increment_pointer_after_dereference)x.p("++");
		x.p(ws_trailing);
	}
	final private String ws_leading,ws_trailing;
	final private expression expr_to_dereference;
	final boolean is_increment_pointer_after_dereference;
	private static final long serialVersionUID=1;
}