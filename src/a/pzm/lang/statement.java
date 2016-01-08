package a.pzm.lang;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class statement extends a{
//	final public static LinkedHashMap<String,String>no_annotations=new LinkedHashMap<>();
	private static final long serialVersionUID=1;
	final protected statement parent_statement;
	private String location_in_source;
	private String location_in_source_end;
	private LinkedHashMap<String,String>annotations;
	final protected String token;
	private String ws_trailing;
	private String ws_after_open_block;
	final private String ws_after_assign;
	private ArrayList<statement>statements;
//	protected ArrayList<String>declarations;
	ArrayList<String>vars;
	private statement expr;

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
		ws_trailing="";
		location_in_source=loc;
		parent_statement=parent;
		ws_after_open_block="";
		ws_after_assign="";
	}
	public statement(a pt,String nm,LinkedHashMap<String,String>annotations,String token,reader r,statement parent){
		super(pt,nm,token);
		mark_start_of_source(r);
		parent_statement=parent;
		this.annotations=annotations;
		this.token=token;
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
		ws_after_open_block="";
		ws_after_assign="";
	}
	@SuppressWarnings("unchecked")
	public statement(a pt,String nm,statement parent,String tk,reader r){
		super(pt,nm);
		parent_statement=parent;
		if(tk==null)r.set_location_in_source();
		mark_start_of_source(r);
		if(r.is_next_char_block_open()){// read block and return
			int i=0;
			ws_after_open_block=r.next_empty_space();
			while(true){
				if(r.next_empty_space().length()!=0)throw new Error();
				if(r.is_next_char_block_close()){
					mark_end_of_source(r);
					ws_trailing=r.next_empty_space();
					ws_after_assign="";
					token="";
					return;
				}
				final String id="i-"+i++;
				final statement d=new statement(this,id,this,null,r);
				statements_add(d);
			}
		} 
		// single statement
		// read annotations
		LinkedHashMap<String,String>annot=null;
		while(true){
			final String s=r.next_token();
			if(s.length()>0){
				if(s.startsWith("@")){//annotation
					final String ws=r.next_empty_space();
					if(annot==null)annot=new LinkedHashMap<>();
					annot.put(s.substring(1),ws);
					continue;
				}
			}
			token=s;
			break;
		}
//		r.set_location_in_source();
		if(token.length()==0){// end of tokens
			if(r.is_next_char_block_open()){// open block  ie:  @dotta{0xfee}
				int i=0;
				annotations=annot;
				ws_after_open_block=r.next_empty_space();
				while(true){
					if(r.next_empty_space().length()!=0)throw new Error();
					if(r.is_next_char_block_close()){
						mark_end_of_source(r);
						ws_trailing=r.next_empty_space();
						ws_after_assign="";
						return;
					}
					r.set_location_in_source();
					final statement d=new statement(this,"i-"+i++,this,null,r);
					statements_add(d);
				}
			}
		}
		if("var".equals(token)){
			r.set_location_in_source();
			expr=new def_var(this,"var",parent,annot,r);
			ws_after_assign="";
			r.set_location_in_source();
			return;
		}
		if("def".equals(token)){
			expr=new def(this,"def",parent,annot,r);
			ws_after_assign="";
			r.set_location_in_source();
			return;
		}
		if(r.is_next_char_assign()){
			ws_after_assign=r.next_empty_space();
			expr=new expression(this,"tx",parent,annot,r,token);
			r.set_location_in_source();
			return;
		}
		ws_after_assign="";
		if(!r.is_next_char_expression_open()){
//			if(r.is_next_char_assign()){
				expr=new expression(this,"e",parent,annot,token,r,null);
//				annotations.clear();
//			}else{
//				r.bm();
//				throw new compiler_error(this,"expected '='","");
//			}
			return;
		}
		final String asm="li stc lp inc add addi ldc ldd ld tx sub shf neg foo fow";
		if(asm.indexOf(token)==-1){
			expr=new call(this,token,parent,annot,token,r);
			r.set_location_in_source();
			return;
		}
		try{
			final String clsnm=getClass().getPackage().getName()+".call_"+token;
			final Class<?>cls=Class.forName(clsnm);
			final Constructor<?>ctor=cls.getConstructor(a.class,String.class,LinkedHashMap.class,reader.class,statement.class);
			expr=(statement)ctor.newInstance(this,"e",annot,r,parent);
			r.set_location_in_source();
			return;
		}catch(Throwable t){
			final Throwable tt=t.getCause();
			throw new Error(tt==null?t:tt);
		}
	}
	private void ensure_annotations_exists() {
		if(annotations!=null)return;
		annotations=new LinkedHashMap<>();
	}
	private void annotations_put(String substring, String ws) {
		ensure_annotations_exists();
		annotations.put(substring,ws);
	}
	private void statements_add(statement d){
		ensure_statements_list_exists();
		statements.add(d);
	}
	private void ensure_statements_list_exists() {
		if(statements!=null)return;
		statements=new ArrayList<>();
	}
	public void binary_to(xbin x){
		if(expr!=null)expr.binary_to(x);
		if(statements!=null)statements.forEach(e->e.binary_to(x));
		if(vars!=null){vars.forEach(e->x.unalloc(this,e));vars.clear();}
	}
	public void source_to(xwriter x){
		if(annotations!=null)annotations.entrySet().forEach(me->x.p('@').p(me.getKey()).p(me.getValue()));
		if(expr!=null){expr.source_to(x);return;}
		if(statements!=null){
			if(!statements.isEmpty()){
				x.p('{').p(ws_after_open_block);
				statements.forEach(e->e.source_to(x));
				x.p('}').p(ws_trailing);
			}
			return;
		}
		x.p(token).p(ws_trailing);
	}
	final @Override public void to(xwriter x) throws Throwable{
		x.style(this,"border:1px dotted blue");
		x.divo(this);
		source_to(x);
		x.div_();
	}
	public boolean has_annotation(String src){
		if(annotations==null)return false;
		return expr==null?annotations.containsKey(src):expr.has_annotation(src);
	}
	public String location_in_source(){
		return location_in_source;
	}
	public String location_in_source_end(){
		return location_in_source_end;
	}
	public void mark_end_of_source(final reader r){
		r.set_location_in_source();
		location_in_source_end=r.location_in_source();
	}
	public void mark_start_of_source(final reader r){
		location_in_source=r.location_in_source();
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
//	public boolean is_register_declared(String register_name){
//		final boolean yes=declarations.contains(register_name);
//		if(yes)return true;
//		if(parent_statement==null)return false;
//		return parent_statement.is_register_declared(register_name);
//	}
	@Override public String toString(){
		final xwriter x=new xwriter();
		source_to(x);
		return x.toString();
	}
	public void vars_add(String name){
		ensure_vars_exists();
		vars.add(name);
	}
	private void ensure_vars_exists() {
		if(vars!=null)return;
		vars=new ArrayList<>();
	}

}