package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class def_table extends def{
	public def_table(statement parent,LinkedHashMap<String,String>annot,reader r,String name,String ws_before_name,String ws_after_name)throws Throwable{
		super(parent, annot);
		nm(name);
		mark_start_of_source(r);
		this.ws_before_name=ws_before_name;
		this.name=name;
		this.ws_after_name=ws_after_name;
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_struct_close())break;
			r.set_location_in_source();
			final def_table_col sf=new def_table_col(this,null,r);
			for(final def_table_col e:arguments){
				if(sf.token.equals(e.token)){
					throw new compiler_error(sf,"column '"+sf.token+"'already exists",name+arguments.toString());
				}
			}
//			if(arguments.stream().filter(e->sf.token.equals(e.token)).findFirst().isPresent()){
//				throw new compiler_error(sf,"column '"+sf.token+"'already exists",name+arguments.toString());
//			}
			arguments.add(sf);
		}
		data=statement.read(this,r);
		mark_end_of_source_from(data);
		r.toc.put("table "+name,this);
	}
	@Override public void binary_to(xbin x){
		x.def(name);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("def").p(ws_before_name).p(name).p(ws_after_name);
		x.p("[");
//		arguments.forEach(e -> e.source_to(x));
		for(final def_table_col e:arguments){
			e.source_to(x);
		}
		x.p("]");
		data.source_to(x);
	}
	final ArrayList<def_table_col>arguments=new ArrayList<>();
	final private String ws_before_name,name,ws_after_name;
	final private statement data;
	private static final long serialVersionUID=1;
}