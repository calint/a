package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.a;
import b.xwriter;

final public class def_table extends statement{
	private static final long serialVersionUID=1;
	final private String name;
	final ArrayList<def_table_col>arguments=new ArrayList<>();
	final private statement data;
	final String ws_leading;
	public def_table(statement parent,LinkedHashMap<String,String>annot,reader r,String name)throws Throwable{
		super(parent,annot);
		this.name=name;
		ws_leading=r.next_empty_space();
		while(true){
			if(r.is_next_char_struct_close())break;
			final def_table_col sf=new def_table_col(this,null,r);
			if(arguments.stream().filter(e->sf.token.equals(e.token)).findFirst().isPresent()){
				throw new compiler_error(sf,"column '"+sf.token+"'already exists",name+arguments.toString());
			}
			arguments.add(sf);
		}
		data=statement.read(this,r);
		mark_end_of_source(r);
		r.toc.put("table "+name,this);
	}
	@Override public void binary_to(xbin x){
		x.def(name);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		x.p("[");
		super.source_to(x);
		arguments.forEach(e->e.source_to(x));
		x.p("]");
		data.source_to(x);
	}
}