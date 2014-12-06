package a.pz.ba;

import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public final class data extends statement{
	private static final long serialVersionUID=1;
	final private statement expr;
	public data(a pt,String nm,reader r){
		super(pt,nm,no_annotations,"",r);
		final LinkedHashMap<String,String> annotations=new LinkedHashMap<>();
		final String src;
		while(true){
			final String s=r.next_token();
			if(s.length()==0) throw new Error("unexpected empty token");
			if(s.startsWith("@")){//annotation
				final String ws=r.next_empty_space();
				annotations.put(s.substring(1),ws);
				continue;
			}
			src=s;
			break;
		}
		if("def".equals(src)){
			expr=new def(this,"e",annotations,r);
			return;
		}
		if(r.is_next_char_expression_open()){
			if("li".equals(src)){
				expr=new call_li(this,"e",annotations,r);
			}else if("st".equals(src)){
				expr=new call_st(this,"e",annotations,r);
			}else if("stc".equals(src)){
				expr=new call_stc(this,"e",annotations,r);
			}else if("lp".equals(src)){
				expr=new call_lp(this,"e",r);
			}else if("inc".equals(src)){
				expr=new call_inc(this,"e",annotations,r);
			}else if("add".equals(src)){
				expr=new call_add(this,"e",annotations,r);
			}else if("ldc".equals(src)){
				expr=new call_ldc(this,"e",annotations,r);
			}else if("ld".equals(src)){
				expr=new call_ld(this,"e",annotations,r);
			}else if("foo".equals(src)){
				expr=new call_foo(this,"e",r);
			}else if("fow".equals(src)){
				expr=new call_fow(this,"e",r);
			}else if("tx".equals(src)){
				expr=new call_tx(this,"e",annotations,r);
			}else if("sub".equals(src)){
				expr=new call_sub(this,"e",annotations,r);
			}else if("shf".equals(src)){
				expr=new call_shf(this,"e",annotations,r);
			}else{
				expr=new call(this,"e",annotations,src,r);
			}
			return;
		}
		expr=new expression(this,"e",src,r);
	}
	@Override public void binary_to(xbin x){
		expr.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		expr.source_to(x);
	}
}