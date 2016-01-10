package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

public class expression extends statement{
	private static final long serialVersionUID=1;
	final private String ws_leading,ws_after;
	final private String destreg;
	boolean is_assign;//? 
	public expression(statement parent,LinkedHashMap<String,String>annot,reader r,String dest_reg,String tk){
		super(parent,annot);
		destreg=dest_reg;
		if(tk==null){// first token supplied
			mark_start_of_source(r);
			ws_leading=r.next_empty_space();
			token=r.next_token();
			mark_end_of_source(r);
		}else{
			mark_start_of_source(r);
			ws_leading="";
			token=tk;
			mark_end_of_source(r);
		}
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
//		destreg=dest_reg;
	}
	@Override public void binary_to(xbin x){
		if(x.is_alias_declared(token)){// tx
			final int rai=x.register_index_for_alias(this,token);
			final int rdi=x.register_index_for_alias(this,destreg);
			x.write(0|0x00e0|(rai&63)<<8|(rdi&63)<<14);//tx(b a)
			return;
		}
		if(destreg!=null){// li
			final int rai=x.register_index_for_alias(this,destreg);
			x.write(0|0x0000|(rai&63)<<14);//li(rai ____)
		}
		x.at_pre_link_evaluate(this);
		x.write(0);
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
		x.p(ws_leading).p(token).p(ws_after);
	}
}