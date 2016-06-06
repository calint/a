package a.pzm.lang;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import b.a;
import b.xwriter;

public class statement extends a{
	final public static statement read(final statement parent,final reader r)throws Throwable{
		final String ws_leading=r.next_empty_space();
		r.set_location_in_source();
		final LinkedHashMap<String,String>annot=r.read_annotation();
		if(r.is_next_char_block_open())return new block(parent,annot,ws_leading,r);
		final String tk=r.next_token();
		if("var".equals(tk))return new var(parent,annot,r);
		if("def".equals(tk))return def.read(parent,annot,r);
		if(r.is_next_char_assign()){// ie  tick=3
			final expression_assign e=new expression_assign(parent,annot,tk,r);
			return e;
		}
		// not function call, ie   0xfada
		if(!r.is_next_char_expression_open()){
			final expression e=new expression(parent,annot,tk,r,null);
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
				if(tt!=null)throw tt;else throw t;
			}
		}
		// function call
		final call e=new call(parent,annot,tk,r);
		return e;
	}
	final public static statement read(final reader r)throws Throwable{return read(null,r);}

	
	public statement(){}
	public statement(final statement parent){super(parent,null,null);}
	public statement(final statement parent,final LinkedHashMap<String,String>annot){super(parent,null,null);this.annotations=annot;}
	final public String token(){return token;}
	final public statement parent_statement(){return (statement)pt();}
	public void binary_to(final xbin x){}
	public void source_to(final xwriter x){
		if(annotations==null)return;
		for(final Map.Entry<String,String>me:annotations.entrySet())x.p('@').p(me.getKey()).p(me.getValue());
	}
	final @Override public void to(final xwriter x) throws Throwable{
		x.style(this,"border:1px dotted blue");
		x.divo(this);
		source_to(x);
		x.div_();
	}
	final public boolean has_annotation(final String src){if(annotations==null)return false;return annotations.containsKey(src);}
	final public String location_in_source(){return source_location_start;}
	final public int source_lineno(){return Integer.parseInt(location_in_source().split(":")[0]);}
	final public int[]source_selection(){final int[]d=new int[2];d[0]=Integer.parseInt(location_in_source().split(":")[2]);d[1]=Integer.parseInt(location_in_source_end().split(":")[2]);return d;}
	final public String location_in_source_end(){return source_location_end;}
	final public void mark_end_of_source(final reader r){r.set_location_in_source();source_location_end=r.location_in_source();}
	final public void mark_end_of_source_from(statement e){source_location_end=e.source_location_end;}
	final public void mark_start_of_source(final reader r){source_location_start=r.location_in_source();}
	final @Override public String toString(){final xwriter x=new xwriter();source_to(x);return x.toString();}
	final public void vars_add(String name){ensure_vars_exists();vars.add(name);}
	final public Map<String,String>annotations(boolean return_empty_if_null){if(annotations==null&&return_empty_if_null)return Collections.emptyMap();return annotations;}
	final public boolean has_annotations(){return annotations!=null&&!annotations.isEmpty();}
	private void ensure_vars_exists(){if(vars!=null)return;vars=new ArrayList<>();}

	private String source_location_start;
	private String source_location_end;
	protected LinkedHashMap<String,String>annotations;
	protected String token;
	protected ArrayList<String>vars;
	final static String asm="li stc lp inc add addi ldc ldd ld tx sub shf neg not and   foo fow";
	private static final long serialVersionUID=1;
}