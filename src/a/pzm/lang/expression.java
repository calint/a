package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

final public class expression extends statement{
	private static final long serialVersionUID=1;
//	final String token;
	private final String ws_after;
	public expression(a pt,String nm,LinkedHashMap<String,String>annotations,reader r,statement b){
		super(pt,nm,annotations,r.next_token(),r,b);
//		token=r.next_token();
		ws_after=r.next_empty_space();
	}
	public expression(a pt,String nm,LinkedHashMap<String,String>annotations,String token,reader r,statement b){
		super(pt,nm,annotations,token,r,b);
//		this.token=token;
		ws_after=r.next_empty_space();
	}
	public expression(String token){super(null,"",null,"",token,null);ws_after="";}
	@Override public void binary_to(xbin x){
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
				throw new compiler_error(this,"not a hex",token);
			}
		}else if(token.startsWith("0b")){
			try{
				return Integer.parseInt(token.substring(2),2);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not a binary",token);
			}
		}else if(token.endsWith("h")){
			try{
				return Integer.parseInt(token.substring(0,token.length()-1),16);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not a hex",token);
			}
		}else{
			try{
				return Integer.parseInt(token);
			}catch(NumberFormatException e){
				throw new compiler_error(this,"not a number",token);
			}
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
//		x.p(token).p(ws_after);
		x.p(ws_after);
	}
}