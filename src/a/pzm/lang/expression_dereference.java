package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.xwriter;

public class expression_dereference extends expression{
	public expression_dereference(statement parent,annotations annot,reader r,String dest_reg){
		super(parent,annot,new token("","*",""),r,dest_reg);
		ws_leading=r.next_empty_space();
		mark_start_of_source(r);
		expr_to_dereference=expression.read(parent,annot,dest_reg,r);
		if(r.is_next_char_plus()){// maybe op plus    *ra+3
			if(r.is_next_char_plus()){// *ra++
				is_increment_pointer_after_dereference=true;
			}else{
				throw new compiler_error(this,"plus not implemented",null);
			}
		}else{
			is_increment_pointer_after_dereference=false;
		}
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
	}
	@Override public void binary_to(xbin x){
		if(destreg==null)throw new compiler_error(this,"expected destionation register",null);
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
	public int eval(xbin b){
//		final def_const dc=(def_const)b.toc.get("const "+token);
//		if(dc!=null){return dc.expr.eval(b); }
		final def_data dd=(def_data)b.toc.get("data "+token.name);
		if(dd!=null){return b.def_location_in_binary_for_name(token.name); }
		final def_table dt=(def_table)b.toc.get("table "+token.name);
		if(dt!=null){return b.def_location_in_binary_for_name(token.name); }
		if(token.name.startsWith("0x")){
			try{
				return Integer.parseInt(token.name.substring(2),16);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not found or not a hex",token.name);
			}
		}else if(token.name.startsWith("0b")){
			try{
				return Integer.parseInt(token.name.substring(2),2);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not found or not a binary",token.name);
			}
		}else if(token.name.endsWith("h")){
			try{
				return Integer.parseInt(token.name.substring(0,token.name.length()-1),16);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not found or not a hex",token.name);
			}
		}else{
			try{
				return Integer.parseInt(token.name);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"'"+token+"' not found or not an integer",token.name);
			}
		}
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