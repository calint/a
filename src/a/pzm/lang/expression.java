package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

public class expression extends statement{
	private static final long serialVersionUID=1;
	final private String ws_leading,ws_after;
	final private String destreg;
	boolean is_assign;//? 
	final boolean is_pointer_dereference;
//	final String pointer;
	public expression(statement parent,LinkedHashMap<String,String>annot,reader r,String dest_reg,String tk){
		super(parent,annot);
		destreg=dest_reg;
		mark_start_of_source(r);
		if(tk==null){// first token supplied
			ws_leading=r.next_empty_space();
			is_pointer_dereference=r.is_next_char_pointer_dereference();
			token=r.next_token();
		}else{
			is_pointer_dereference=false;
			ws_leading="";
			token=tk;
		}
		mark_end_of_source(r);
		if(token.length()==0)
			throw new compiler_error(this,"expression is empty","");
		ws_after=r.next_empty_space();
	}
//	public expression(a pt,String nm,statement b,LinkedHashMap<String,String>annotations,reader r,String dest_reg){
//		super(pt,nm,annotations,r.next_token(),r,b);
////		mark_end_of_source(r);
//		ws_after="";
//		this.destreg=dest_reg;
//	}
	public expression(statement parent,String dest_reg){
		super(parent,null);
		ws_after=ws_leading="";
		token=dest_reg;
		destreg=null;
		is_pointer_dereference=false;
//		destreg=dest_reg;
	}
	@Override public void binary_to(xbin x){
		if(is_pointer_dereference){// ld or ldc
			if(x.vspc().is_declared(token)){// ld(ra rd)
				final int rai=x.vspc().get_register_index(this,token);
				final int rdi=x.vspc().get_register_index(this,destreg);
				x.write_op(this,call_ld.op,rai,rdi);
				return;
			}
			// ld(0xf00 4)
			final String reg=x.alloc_register(this);
			final int regi=x.get_register_index_for_name(reg);
			x.write_op(this,call_li.op,0,regi);
			x.add_at_pre_link_evaluate(this);
			x.write(0,this);
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_ld.op,regi,rdi);
			x.free_register(this,reg);
			return;
		}
		if(x.vspc().is_declared(token)){// tx
			final int rai=x.vspc().get_register_index(this,token);
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_tx.op,rai,rdi);
			return;
		}
		if(destreg!=null){// li
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_li.op,0,rdi);
		}
		x.add_at_pre_link_evaluate(this);
		x.write(0,this);
	}
	public int eval(xbin b){
//		final def_const dc=(def_const)b.toc.get("const "+token);
//		if(dc!=null){return dc.expr.eval(b); }
		final def_data dd=(def_data)b.toc.get("data "+token);
		if(dd!=null){return b.def_location_in_binary_for_name(token); }
		final def_table dt=(def_table)b.toc.get("table "+token);
		if(dt!=null){return b.def_location_in_binary_for_name(token); }
		if(token.startsWith("0x")){
			try{
				return Integer.parseInt(token.substring(2),16);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not found or not a hex",token);
			}
		}else if(token.startsWith("0b")){
			try{
				return Integer.parseInt(token.substring(2),2);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not found or not a binary",token);
			}
		}else if(token.endsWith("h")){
			try{
				return Integer.parseInt(token.substring(0,token.length()-1),16);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not found or not a hex",token);
			}
		}else{
			try{
				return Integer.parseInt(token);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"'"+token+"' not found or not an integer",token);
			}
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		if(is_assign){
			x.p(destreg).p("=");
		}
		x.p(ws_leading);
		if(is_pointer_dereference)x.p("*");
		x.p(token).p(ws_after);
	}
}