package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

public class expression extends statement{
	public static expression read(statement parent,LinkedHashMap<String,String>annot,String dest_reg,reader r){
//		final ArrayList<expression>addition_list=new ArrayList<>();
//		final String ws_leading=r.next_empty_space();
		if(r.is_next_char_pointer_dereference()){
			final expression_dereference e=new expression_dereference(parent,annot,r,dest_reg);
			return e;
		}
		final String token=r.next_token();
		if(r.is_next_char_expression_open()){// function call
			final call e=new call(parent,annot,token,r,dest_reg);
			return e;
		}
		
		return new expression(parent,annot,token,r,dest_reg);
	}
	
	
	
	private static final long serialVersionUID=1;
	final private String ws_leading,ws_trailing;
	String destreg;
//	boolean is_assign; 
	protected expression(statement parent,LinkedHashMap<String,String>annot,String tk,reader r,String dest_reg){
		super(parent,annot);
		destreg=dest_reg;
		mark_start_of_source(r);
		if(tk==null){
			ws_leading=r.next_empty_space();
			token=r.next_token();
		}else{// first token supplied
			ws_leading="";
			token=tk;
		}
		mark_end_of_source(r);
		if(token.length()==0)
			throw new compiler_error(this,"expression is empty","");
		ws_trailing=r.next_empty_space();
	}
	protected expression(statement parent,LinkedHashMap<String,String>annot,String tkn,String dest_reg){
		super(parent,annot);
		ws_trailing=ws_leading="";
		token=tkn;
		destreg=dest_reg;
	}
	expression(statement parent,String dest_reg){
		super(parent,null);
		ws_trailing=ws_leading="";
		token=dest_reg;
		destreg=null;
	}
	@Override public void binary_to(xbin x){		
		if(x.vspc().is_declared(token)){// tx
			final int rai=x.vspc().get_register_index(this,token);
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_tx.op,rai,rdi);
			return;
		}
		
		if(destreg!=null){// li
			final int rdi=x.vspc().get_register_index(this,destreg);
			x.write_op(this,call_li.op,0,rdi);
			x.at_link_eval(this);
			x.write(0,this);
			return;
		}
		
		
		// constant
		x.at_link_eval(this);
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
		x.p(ws_leading);
		x.p(token);
		x.p(ws_trailing);
	}
}