package a.pzm.lang;

import java.util.ArrayList;

import a.pzm.lang.reader.token;
import b.xwriter;

final public class def_table extends def{
	public def_table(statement parent,annotations annot,token deftkn,token tk,reader r)throws Throwable{
		super(parent,annot,deftkn,r);
		nm(tk.name);
		identtkn=tk;
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_struct_close())break;
			r.set_location_cue();
			final def_table_col tc=new def_table_col(this,null,r);
			for(final def_table_col e:arguments){
				if(tc.token.equals(e.token)){
					throw new compiler_error(tc,"column '"+tc.token+"'already exists",token.name+arguments.toString());
				}
			}
//			if(arguments.stream().filter(e->sf.token.equals(e.token)).findFirst().isPresent()){
//				throw new compiler_error(sf,"column '"+sf.token+"'already exists",name+arguments.toString());
//			}
			arguments.add(tc);
		}
		data=statement.read(this,r);
		mark_end_of_source_from(data);
		r.toc.put("table "+identtkn.name,this);
	}
	@Override public void binary_to(xbin x){
		x.def(identtkn.name);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		identtkn.to(x);
		x.p("[");
		for(final def_table_col e:arguments){
			e.source_to(x);
		}
		x.p("]");
		data.source_to(x);
	}
	final ArrayList<def_table_col>arguments=new ArrayList<>();
	final private token identtkn;
	final private statement data;
	private static final long serialVersionUID=1;
}