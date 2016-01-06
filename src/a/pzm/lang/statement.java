package a.pzm.lang;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class statement extends a{
	final public static LinkedHashMap<String,String>no_annotations=new LinkedHashMap<>();
	private static final long serialVersionUID=1;
	final protected statement parent_statement;
	final private String location_in_source;
	final private LinkedHashMap<String,String>annotations;
	final protected String token;
	final private String ws_after;
	private statement expr;

	final private String ws_after_open_block;
	final private ArrayList<statement>statements=new ArrayList<>();
	final protected ArrayList<String>declarations=new ArrayList<>();
	final ArrayList<String>vars=new ArrayList<>();

//	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String loc,block b){
//		super(pt,nm);
//		this.annotations=annotations;
//		token="";
//		ws_after="";
//		location_in_source=loc;
//		blk=b;
//	}
	//? use
	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String loc,String token,statement parent){
		super(pt,nm,token);
		this.annotations=annotations;
		this.token=token;
		ws_after="";
		location_in_source=loc;
		parent_statement=parent;
		ws_after_open_block="";
	}
	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String token,reader r,statement b){
		super(pt,nm,token);
		parent_statement=b;
		this.annotations=annotations;
		this.token=token;
		ws_after=r.next_empty_space();
		location_in_source=r.bm_line+":"+r.bm_col;
		ws_after_open_block="";
//		r.bm();
	}
	public statement(a pt,String nm,statement parent,reader r){
		super(pt,nm);
		parent_statement=parent;
		location_in_source=r.bm_line+":"+r.bm_col;
		token="";
		annotations=new LinkedHashMap<>();
		final String token;
		r.bm();
		if(r.is_next_char_block_open()){
			int i=0;
			ws_after_open_block=r.next_empty_space();
			while(true){
				//				pl("line :"+i);
				if(r.next_empty_space().length()!=0)throw new Error();
				if(r.is_next_char_block_close())break;
				final statement d=new statement(this,"i-"+i++,this,r);
				statements.add(d);
			}
			ws_after=r.next_empty_space();
			return;
		} 
		ws_after_open_block=ws_after="";
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
			expr=new var(this,"e",r,parent_statement);
			return;
		}
		if("def".equals(token)){
			expr=new def(this,"e",annotations,r,parent);
			return;
		}
		if(!r.is_next_char_expression_open()){
			expr=new expression(this,"e",annotations,token,r,parent);
			return;
		}
		final String asm="li stc lp inc add ldc ld tx sub shf  foo fow";
		if(asm.indexOf(token)==-1){
			expr=new call(this,"e",annotations,token,r,parent);
			return;
		}
		try{
			final String clsnm=getClass().getPackage().getName()+".call_"+token;
			final Class<?>cls=Class.forName(clsnm);
			final Constructor<?>ctor=cls.getConstructor(a.class,String.class,LinkedHashMap.class,reader.class,statement.class);
//			System.out.println("instr "+token);
			expr=(statement)ctor.newInstance(this,"e",annotations,r,parent);
			return;
		}catch(Throwable t){
			final Throwable tt=t.getCause();
			throw new Error(tt==null?t:tt);
		}
	}
	public void binary_to(xbin x){
		if(expr!=null)
			expr.binary_to(x);
		statements.forEach(e->e.binary_to(x));
		vars.forEach(e->x.unalloc(this,e));
		vars.clear();
	}
	public void source_to(xwriter x){
		annotations.entrySet().forEach(me->x.p('@').p(me.getKey()).p(me.getValue()));
		x.p(token).p(ws_after);
		if(expr!=null)
			expr.source_to(x);
		if(!statements.isEmpty()){
			x.p('{').p(ws_after_open_block);
			statements.forEach(e->e.source_to(x));
			x.p('}').p(ws_after);
		}
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
			if(s.length()==0)throw new Error("unexpected empty token");
			final String ws=r.next_empty_space();
			annotations.put(s,ws);
		}
		return annotations;
	}
	public boolean is_register_declared(String register_name){
		final boolean yes=declarations.contains(register_name);
		if(yes)return true;
		if(parent_statement==null)return false;
		return parent_statement.is_register_declared(register_name);
	}
}