package a.pz.ba;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public final class data extends statement{
	private static final long serialVersionUID=1;
	final private statement expr;
	public data(a pt,String nm,reader r){
		super(pt,nm,no_annotations,"",r);
		final LinkedHashMap<String,String> annotations=new LinkedHashMap<>();
		final String token;
		while(true){
			final String s=r.next_token();
			if(s.length()==0) throw new Error("unexpected empty token");
			if(s.startsWith("@")){//annotation
				final String ws=r.next_empty_space();
				annotations.put(s.substring(1),ws);
				continue;
			}
			token=s;
			break;
		}
		if("def".equals(token)){
			expr=new def(this,"e",annotations,r);
			return;
		}
		if(r.is_next_char_expression_open()){
			final String asm="li stc lp inc add ldc ld foo fow tx sub shf";
			if(asm.indexOf(token)==-1){
				expr=new call(this,"e",annotations,token,r);
				return;
			}
			try{
				final String clsnm=getClass().getPackage().getName()+".call_"+token;
				final Class<?> cls=Class.forName(clsnm);
				final Constructor<?> ctor=cls.getConstructor(a.class,String.class,LinkedHashMap.class,reader.class);
				expr=(statement)ctor.newInstance(this,"e",annotations,r);
				return;
			}catch(Throwable t){
				throw new Error(t);
			}
//			if("li".equals(token)){
//				expr=new call_li(this,"e",annotations,r);
//			}else if("st".equals(token)){
//				expr=new call_st(this,"e",annotations,r);
//			}else if("stc".equals(token)){
//				expr=new call_stc(this,"e",annotations,r);
//			}else if("lp".equals(token)){
//				expr=new call_lp(this,"e",annotations,r);
//			}else if("inc".equals(token)){
//				expr=new call_inc(this,"e",annotations,r);
//			}else if("add".equals(token)){
//				expr=new call_add(this,"e",annotations,r);
//			}else if("ldc".equals(token)){
//				expr=new call_ldc(this,"e",annotations,r);
//			}else if("ld".equals(token)){
//				expr=new call_ld(this,"e",annotations,r);
//			}else if("foo".equals(token)){
//				expr=new call_foo(this,"e",annotations,r);
//			}else if("fow".equals(token)){
//				expr=new call_fow(this,"e",annotations,r);
//			}else if("tx".equals(token)){
//				expr=new call_tx(this,"e",annotations,r);
//			}else if("sub".equals(token)){
//				expr=new call_sub(this,"e",annotations,r);
//			}else if("shf".equals(token)){
//				expr=new call_shf(this,"e",annotations,r);
//			}else{
//				expr=new call(this,"e",annotations,token,r);
//			}
//			return;
		}
		expr=new expression(this,"e",annotations,token,r);
	}
	@Override public void binary_to(xbin x){
		expr.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		expr.source_to(x);
	}
}