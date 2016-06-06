package a.pzm.lang;

import java.util.ArrayList;

import b.xwriter;

public class block extends statement{
	public block(statement parent,annotations annot,reader r)throws Throwable{
		super(parent,annot);
		mark_start_of_source(r);
		while(true){
			if(r.is_next_char_block_close()){
				mark_end_of_source(r);
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
		super.source_to(x);
		x.p("{");
		if(statements!=null)for(statement s:statements){
			s.source_to(x);
		}
		x.p("}");
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
	private ArrayList<statement>statements;
	private static final long serialVersionUID=1;
}
