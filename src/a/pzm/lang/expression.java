package a.pzm.lang;

import a.pzm.lang.reader.token;

public class expression extends statement{
	public static expression read(statement parent,annotations annot,String destreg,reader r){
//		final ArrayList<expression>addition_list=new ArrayList<>();
//		final String ws_leading=r.next_empty_space();
		if(r.is_next_char_pointer_dereference()){
			final expression_dereference e=new expression_dereference(parent,annot,r,destreg);
			e.mark_end_of_source(r);
			return e;
		}
		final token tk=r.next_token();
		if(r.is_next_char_expression_open()){// function call
			final call e=new call(parent,annot,tk,r,destreg);
			e.mark_end_of_source(r);
			return e;
		}
		
		final expression e=new expression(parent,annot,tk,r,destreg);
		e.source_location_end=r.token_end_loc.toString();
		return e;
	}
	
	
	//------     - --  - - -- - ---- --------- - - -- - - -- - - - - -- - -- - - - -- 	
	protected expression(statement parent,annotations annot,token tk,reader r,String dest_reg){
		super(parent,annot,tk,r);
		if(token.name.length()==0)throw new compiler_error(this,"expression is empty","");
		destreg=dest_reg;
	}
	expression(statement parent,String dest_reg){
		super(parent,null,new token("",dest_reg,""),null);
		destreg=null;
	}
	@Override public void binary_to(xbin x){		
		if(x.vspc().is_declared(token.name)){// tx
			final int rai=x.vspc().get_register_index(this,token.name);
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
				throw new compiler_error(this,"'"+token.name+"' not found or not an integer",token.name);
			}
		}
	}
	String destreg;
	private static final long serialVersionUID=1;
}