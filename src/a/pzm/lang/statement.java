package a.pzm.lang;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import b.a;
import b.xwriter;

public class statement extends a{
	private static final long serialVersionUID=1;
	private String source_location_start;
	private String source_location_end;
	protected LinkedHashMap<String,String>annotations;
	protected String token;
	protected ArrayList<String>vars;//
	private statement expr;// the actual expression
//	private boolean is_assign;// rega=regb

	public statement(){}
	public statement(statement parent){super(parent,null,null);}
	public statement(statement parent,LinkedHashMap<String,String>annot){
		super(parent,null,null);
		this.annotations=annot;
	}
	public statement parent_statement(){
		return (statement)pt();
	}
	public static statement read(reader r)throws Throwable{
		return read(null,r);
	}
	public static statement read(statement parent,reader r)throws Throwable{
		final String ws_leading=r.next_empty_space();
		r.set_location_in_source();
		final LinkedHashMap<String,String>annot=read_annot(r);
		if(r.is_next_char_block_open()){
			final block blk=new block(parent,annot,ws_leading,r);
			return blk;
		}
		final String tk=r.next_token();
		if("var".equals(tk)){
			final var e=new var(parent,annot,r);
			return e;
		}
		if("def".equals(tk)){
			final def e=def.read(parent,annot,r);
			return e;
		}
		// assign
		if(r.is_next_char_assign()){// ie  tick=3
			r.set_location_in_source();
			final expression e=new expression(parent,null,r,tk,null);
			e.is_assign=true;
			return e;
		}
		// not function call, ie   0xfada
		if(!r.is_next_char_expression_open()){
			final expression e=new expression(parent,annot,r,null,tk);
			return e;
		}
		//assembler op
		if(asm.indexOf(tk)!=-1){
			try{
				final String clsnm=statement.class.getPackage().getName()+".call_"+tk;
				final Class<?>cls=Class.forName(clsnm);
				final Constructor<?>ctor=cls.getConstructor(statement.class,LinkedHashMap.class,reader.class);
				final statement s=(statement)ctor.newInstance(parent,annot,r);
				return s;
			}catch(Throwable t){
				final Throwable tt=t.getCause();
				throw new Error(tt==null?t:tt);
			}
		}
		// function call
		final call e=new call(parent,annot,tk,r);
		return e;
	}
	final public static String asm="li stc lp inc add addi ldc ldd ld tx sub shf neg not and   foo fow";
//	private void ensure_annotations_exists() {
//		if(annotations!=null)return;
//		annotations=new LinkedHashMap<>();
//	}
//	private void annotations_put(String substring, String ws) {
//		ensure_annotations_exists();
//		annotations.put(substring,ws);
//	}
	public void binary_to(xbin x){}
	public void source_to(xwriter x){
		if(annotations!=null)annotations.entrySet().forEach(me->x.p('@').p(me.getKey()).p(me.getValue()));
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
		return source_location_start;
	}
	public int source_lineno(){
		return Integer.parseInt(location_in_source().split(":")[0]);
	}
	public int[]source_selection(){
		final int[]d=new int[2];
		d[0]=Integer.parseInt(location_in_source().split(":")[2]);
		d[1]=Integer.parseInt(location_in_source_end().split(":")[2]);
		return d;
	}
	public String location_in_source_end(){
		return source_location_end;
	}
	public void mark_end_of_source(final reader r){
		r.set_location_in_source();
		source_location_end=r.location_in_source();
	}
	public void mark_end_of_source_from(statement e){
		source_location_end=e.source_location_end;
	}
	public void mark_start_of_source(final reader r){
		source_location_start=r.location_in_source();
	}
	public static LinkedHashMap<String,String>read_annot(reader r){
		LinkedHashMap<String,String>annot=null;
		while(true){
			if(!r.is_next_char_annotation_open())break;
			final String s=r.next_token();
			if(s.length()==0)throw new Error("unexpected empty token");
			final String ws=r.next_empty_space();
			if(annot==null)
				annot=new LinkedHashMap<>();
			annot.put(s,ws);
		}
		return annot;
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
	public Map<String,String>annotations(boolean return_empty_if_null){
		if(annotations==null&&return_empty_if_null)
			return Collections.EMPTY_MAP;
		return annotations;
	}
	public boolean has_annotations(){return annotations!=null&&!annotations.isEmpty();}
}