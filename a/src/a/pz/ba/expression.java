package a.pz.ba;

import b.a;
import b.xwriter;

final public class expression extends statement{
	private static final long serialVersionUID=1;
	final String src;
	private final String ws_after;
	public expression(a pt,String nm,reader r){
		super(pt,nm,r);
		src=r.next_token();
		ws_after=r.next_empty_space();
	}
	public expression(a pt,String nm,String src,reader r){
		super(pt,nm);
		this.src=src;
		ws_after=r.next_empty_space();
	}
	@Override public void binary_to(xbin x){
		//			x.write(eval(x));
		x.evaluate_pre_link(this);
		x.write(0);
	}
	public int eval(xbin b){
		final def_const dc=(def_const)b.toc.get("const "+src);
		if(dc!=null){ return dc.expr.eval(b); }
		final def_data dd=(def_data)b.toc.get("data "+src);
		if(dd!=null){ return b.def_location_in_binary_for_name(src); }
		final def_tuple dt=(def_tuple)b.toc.get("tuple "+src);
		if(dt!=null){ return b.def_location_in_binary_for_name(src); }
		if(src.startsWith("0x")){
			try{
				return Integer.parseInt(src.substring(2),16);
			}catch(NumberFormatException e){
				throw new Error("not a hex: "+src);
			}
		}else if(src.startsWith("0b")){
			try{
				return Integer.parseInt(src.substring(2),2);
			}catch(NumberFormatException e){
				throw new Error("not a binary: "+src);
			}
		}else if(src.endsWith("h")){
			try{
				return Integer.parseInt(src.substring(0,src.length()-1),16);
			}catch(NumberFormatException e){
				throw new Error("not a hex: "+src);
			}
		}else{
			try{
				return Integer.parseInt(src);
			}catch(NumberFormatException e){
				throw new Error("not a number: "+src);
			}
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p(src).p(ws_after);
	}
}