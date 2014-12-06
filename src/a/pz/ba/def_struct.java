package a.pz.ba;

import java.util.ArrayList;
import b.a;
import b.xwriter;

final public class def_struct extends statement{
	private static final long serialVersionUID=1;
	final private String name;
	final ArrayList<expression> arguments=new ArrayList<>();
	final private block data;
	public def_struct(a pt,String nm,String name,reader r){
		super(pt,nm,no_annotations,"",r);
		this.name=name;
		int i=0;
		while(true){
			if(r.is_next_char_struct_close()) break;
			final expression arg=new expression(this,""+i++,r);
			arguments.add(arg);
		}
		data=new block(this,"d",r);
		r.toc.put("struct "+name,this);
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