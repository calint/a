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
		if(!r.is_next_char_expression_open()){
			expr=new expression(this,"e",annotations,token,r);
			return;
		}
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
	}
	@Override public void binary_to(xbin x){
		expr.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		expr.source_to(x);
	}
}