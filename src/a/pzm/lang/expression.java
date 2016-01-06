package a.pzm.lang;

import java.util.LinkedHashMap;

import b.a;
import b.xwriter;

final public class expression extends statement{
	private static final long serialVersionUID=1;
	private final String ws_after;
	final protected String destreg;
	public expression(a pt,String nm,reader r,statement parent){
		this(pt,nm,parent,new LinkedHashMap<>(),r.next_token(),r,null);
	}
	public expression(a pt,String nm,statement parent,reader r,String dest_reg){
		this(pt,nm,parent,new LinkedHashMap<>(),r.next_token(),r,dest_reg);
	}
	public expression(a pt,String nm,statement b,LinkedHashMap<String,String>annotations,String token,reader r,String dest_reg){
		super(pt,nm,annotations,token,r,b);
		ws_after=r.next_empty_space();
		this.destreg=dest_reg;
	}
	public expression(a pt,String nm,statement b,LinkedHashMap<String,String>annotations,reader r,String dest_reg){
		super(pt,nm,annotations,r.next_token(),r,b);
		ws_after=r.next_empty_space();
		this.destreg=dest_reg;
	}
	public expression(String token){
		super(null,"",null,"",token,null);ws_after="";
		destreg=null;
	}
	@Override public void binary_to(xbin x){
		if(x.alias_exists(token)){// tx
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
		final def_const dc=(def_const)b.toc.get("const "+token);
		if(dc!=null){return dc.expr.eval(b); }
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
				throw new compiler_error(this,"not found or not an integer",token);
			}
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
//		x.p(token).p(ws_after);
		x.p(ws_after);
	}
}