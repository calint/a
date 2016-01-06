package a.pzm.lang;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class statement extends a{
	final public static LinkedHashMap<String,String>no_annotations=new LinkedHashMap<>();
	private static final long serialVersionUID=1;
	final protected String token;
	final private String ws_after;
	final private LinkedHashMap<String,String>annotations;
	final private String location_in_source;
	final protected block blk;
	private statement expr;
//	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String loc,block b){
//		super(pt,nm);
//		this.annotations=annotations;
//		token="";
//		ws_after="";
//		location_in_source=loc;
//		blk=b;
//	}
	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String loc,String token,block b){
		super(pt,nm,token);
		this.annotations=annotations;
		this.token=token;
		ws_after="";
		location_in_source=loc;
		blk=b;
	}
	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String token,reader r,block b){
		super(pt,nm,token);
		this.annotations=annotations;
		this.token=token;
		ws_after=r.next_empty_space();
		location_in_source=r.bm_line+":"+r.bm_col;
		blk=b;
//		r.bm();
	}
	public statement(a pt,String nm,block b,reader r){
		super(pt,nm);
		blk=b;
		location_in_source=r.bm_line+":"+r.bm_col;
		ws_after=token="";
		annotations=new LinkedHashMap<>();
		final String token;
		r.bm();
		while(true){
			final String s=r.next_token();
			if(s.length()==0)throw new Error("unexpected empty token");
			if(s.startsWith("@")){//annotation
				final String ws=r.next_empty_space();
				annotations.put(s.substring(1),ws);
				continue;
			}
			token=s;
			break;
		}
		if("var".equals(token)){
			expr=new var(this,"e",r,b);
			return;
		}
		if("def".equals(token)){
			expr=new def(this,"e",annotations,r,b);
			return;
		}
		if(!r.is_next_char_expression_open()){
			expr=new expression(this,"e",annotations,token,r,b);
			return;
		}
		final String asm="li stc lp inc add ldc ld tx sub shf  foo fow";
		if(asm.indexOf(token)==-1){
			expr=new call(this,"e",annotations,token,r,b);
			return;
		}
		try{
			final String clsnm=getClass().getPackage().getName()+".call_"+token;
			final Class<?>cls=Class.forName(clsnm);
			final Constructor<?>ctor=cls.getConstructor(a.class,String.class,LinkedHashMap.class,reader.class,block.class);
//			System.out.println("instr "+token);
			expr=(statement)ctor.newInstance(this,"e",annotations,r,b);
			return;
		}catch(Throwable t){
			final Throwable tt=t.getCause();
			throw new Error(tt==null?t:tt);
		}
	}
	public void binary_to(xbin x){
		if(expr!=null)
			expr.binary_to(x);
	}
	public void source_to(xwriter x){
		annotations.entrySet().forEach(me->x.p('@').p(me.getKey()).p(me.getValue()));
		x.p(token).p(ws_after);
		if(expr!=null)
			expr.source_to(x);
	}

	final @Override public void to(xwriter x) throws Throwable{
		source_to(x);
	}
//	@Override public String toString(){
//		try{
//			final xwriter x=new xwriter();
//			to(x);
//			return x.toString();
//		}catch(Throwable t){throw new Error(t);}
//	}
	public boolean has_annotation(String src){
		return annotations.containsKey(src);
	}
	public String location_in_source(){
		return location_in_source;
	}
	public static LinkedHashMap<String,String>read_annot(reader r){
		final LinkedHashMap<String,String>annotations=new LinkedHashMap<>();
		while(true){
			if(!r.is_next_char_annotation_open())break;
			final String s=r.next_token();
			if(s.length()==0) throw new Error("unexpected empty token");
			final String ws=r.next_empty_space();
			annotations.put(s,ws);
		}
		return annotations;
	}
}