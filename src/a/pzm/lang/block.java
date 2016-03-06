package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

public class block extends statement{
	final String before_annotations;
	final String ws_after_open_block;
	final String ws_after_close_block;
	private ArrayList<statement>statements;
	public block(statement parent,LinkedHashMap<String,String>annot,String ws_before_annotations,reader r)throws Throwable{
		super(parent,annot);
		this.before_annotations=ws_before_annotations;
		mark_start_of_source(r);
		ws_after_open_block=r.next_empty_space();
		while(true){
			if(r.next_empty_space().length()!=0)throw new Error();
			if(r.is_next_char_block_close()){
				mark_end_of_source(r);
				ws_after_close_block=r.next_empty_space();
				break;
			}
			r.set_location_in_source();
			final statement d=statement.read(this,r);
			statements_add(d);
		}
	}
	public ArrayList<statement>statements(){return statements;}
	private void statements_add(statement d){
		ensure_statements_list_exists();
		statements.add(d);
	}
	private void ensure_statements_list_exists() {
		if(statements!=null)return;
		statements=new ArrayList<>();
	}
	@Override public void source_to(xwriter x){
		x.p(before_annotations);
		super.source_to(x);
		x.p("{").p(ws_after_open_block);
		if(statements!=null)for(statement s:statements){
			s.source_to(x);
		}
		x.p("}").p(ws_after_close_block);
	}
	@Override public void binary_to(xbin x){
		if(statements!=null){
			for(statement s:statements){
				s.binary_to(x);
			}
			if(vars!=null){
//				vars.forEach(e -> x.vspc().free_var(this, e));
				for(final String e:vars){
					x.vspc().free_var(this,e);
				}
				vars.clear();
			}
		}
	}

	private static final long serialVersionUID=1;
}
