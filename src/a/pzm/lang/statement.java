package a.pzm.lang;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import a.pzm.lang.reader.token;
import b.a;
import b.xwriter;

public class statement extends a{
	final public static statement read(final statement parent,final reader r)throws Throwable{
		r.set_location_cue();
		final annotations annot=new annotations(parent,r);
		r.set_location_cue();
		if(r.is_next_char_block_open())
			return new block(parent,annot,r);
		r.set_location_cue();
		final token tk=r.next_token();
		if(tk.name==null||tk.name.isEmpty())
			return new statement(parent,annot,null,r);
		if("var".equals(tk.name))
			return new var(parent,annot,tk,r);
		if("def".equals(tk.name))
			return def.read(parent,annot,tk,r);
		if(r.is_next_char_assign())// ie  tick=3
			return new expression_assign(parent,annot,tk,r);
		if(!r.is_next_char_expression_open())// not function call, ie   0xfada
			return new expression(parent,annot,tk,r,null);
		
		//assembler op
		if(asm.indexOf(tk.name)!=-1){
			try{
				final String clsnm=statement.class.getPackage().getName()+".call_"+tk.name;
				final Class<?>cls=Class.forName(clsnm);
				final Constructor<?>ctor=cls.getConstructor(statement.class,annotations.class,token.class,reader.class);
				final statement s=(statement)ctor.newInstance(parent,annot,tk,r);
				return s;
			}catch(Throwable t){
				final Throwable tt=t.getCause();
				if(tt!=null)throw tt;else throw t;
			}
		}
		// function call
		return new call(parent,annot,tk,r);
	}
	final public static statement read(final reader r)throws Throwable{return read(null,r);}

	//------     - --  - - -- - ---- --------- - - -- - - -- - - - - -- - -- - - - -- 
	public statement(final statement parent,final annotations annot,final token tk,final reader r){
		super(parent,tk!=null?tk.name:null,null);
		annots=annot;
		token=tk;
		if(r!=null){
			source_location_start=source_location_end=r.location_cue();
		}else{
			source_location_start=source_location_end="1:1:1:1";
		}
	}

	final public token token(){return token;}
	final public statement parent_statement(){return (statement)pt();}
	public void binary_to(final xbin x){}
	public void source_to(final xwriter x){
		if(annots!=null)annots.to(x);
		if(token!=null)token.to(x);
	}
	final @Override public void to(final xwriter x) throws Throwable{
		x.style(this,"border:1px dotted blue");
		x.divo(this);
		source_to(x);
		x.div_();
	}
	final public boolean has_annotation(final String tag){if(annots==null)return false;return annots.has_annotation(tag);}
	final public String location_in_source(){return source_location_start;}
	final public int source_lineno(){if(
		source_location_start==null)
			return 1;
		return Integer.parseInt(location_in_source().split(":")[0]);
	}
	final public int[]source_selection(){
		if(source_location_start==null)
			return new int[]{1,1};
		final int[]d=new int[2];
		d[0]=Integer.parseInt(source_location_start.split(":")[2]);
		d[1]=Integer.parseInt(source_location_end.split(":")[2]);
		return d;
	}
	final public String location_in_source_end(){return source_location_end;}
	final public void mark_end_of_source(final reader r){
		source_location_end=r.location_in_source();
	}
	final public void mark_end_of_source_from(statement e){
		source_location_end=e.source_location_end;
	}
	final @Override public String toString(){final xwriter x=new xwriter();source_to(x);return x.toString();}
	final public void vars_add(String name){ensure_vars_exists();vars.add(name);}
	final public annotations annotations(){return annots;}
	final public boolean has_annotations(){return annots!=null&&annots.count()!=0;}
	final public token annotations_get(int index){if(annots==null)return null;return annots.get(index);}
	private void ensure_vars_exists(){if(vars!=null)return;vars=new ArrayList<>();}

	String source_location_start;
	String source_location_end;
	final private annotations annots;
	final protected token token;
	protected ArrayList<String>vars;
	final static String asm="li stc lp inc add addi ldc ldd ld tx sub shf neg not and   foo fow";
	private static final long serialVersionUID=1;
}