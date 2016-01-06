package a.pzm.lang;

import java.util.ArrayList;
import b.a;
import b.xwriter;

final public class def_table extends statement{
	private static final long serialVersionUID=1;
	final private String name;
	final ArrayList<def_table_column> arguments=new ArrayList<>();
	final private statement data;
	public def_table(a pt,String nm,String name,reader r,statement b){
		super(pt,nm,no_annotations,"",r,b);
		this.name=name;
//		int i=0;
		while(true){
			if(r.is_next_char_struct_close()) break;
//			final expression arg=new expression(this,""+i++,no_annotations,r,b);
//			arguments.add(arg);
			final def_table_column sf=new def_table_column(this,"",r,b);
//			final String field=r.next_token();
//			r.next_empty_space();
			arguments.add(sf);
		}
		data=new statement(this,"d",b,r);
		r.toc.put("table "+name,this);
	}
	@Override public void binary_to(xbin x){
		x.def(name);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		x.p("[");
		super.source_to(x);
//		arguments.forEach(e->e.source_to(x));
		arguments.forEach(e->e.source_to(x));
		x.p("]");
		data.source_to(x);
	}
}