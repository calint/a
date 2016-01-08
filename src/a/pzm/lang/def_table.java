package a.pzm.lang;

import java.util.ArrayList;
import b.a;
import b.xwriter;

final public class def_table extends statement{
	private static final long serialVersionUID=1;
	final private String name;
	final ArrayList<def_table_column>arguments=new ArrayList<>();
	final private statement data;
	public def_table(a pt,String nm,String name,reader r,statement b){
		super(pt,nm,null,"",r,b);
		this.name=name;
		while(true){
			if(r.is_next_char_struct_close()) break;
			final def_table_column sf=new def_table_column(this,"",r,b);
			arguments.add(sf);
		}
		data=new statement(this,"d",b,"",r);
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