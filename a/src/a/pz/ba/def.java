package a.pz.ba;

import b.a;
import b.xwriter;

final public class def extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name;
	final private statement e;
	public def(a pt,String nm,reader r){
		super(pt,nm,r);
		name=r.next_token();
		ws_after_name=r.next_empty_space();
		if(r.is_next_char_expression_open()){
			e=new def_func(this,name,name,r);
		}else if(r.is_next_char_block_open()){
			r.unread_last_char();
			e=new def_data(this,name,name,r);
		}else if(r.is_next_char_tuple_open()){
			e=new def_tuple(this,name,name,r);
		}else{
			e=new def_const(this,name,name,r);
		}
	}
	@Override public void binary_to(xbin x){
		e.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		x.p("def");
		super.source_to(x);
		x.tag("def").p(name).tage("def");
		x.p(ws_after_name);
		e.source_to(x);
	}
}