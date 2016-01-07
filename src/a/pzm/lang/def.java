package a.pzm.lang;

import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

final public class def extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name;
	final private statement e;
	public def(a pt,String nm,statement b,LinkedHashMap<String,String>annotations,reader r){
		super(pt,nm,annotations,"def",r,b);
		name=r.next_token();
		ws_after_name=r.next_empty_space();
		if(r.is_next_char_expression_open()){
			e=new def_func(this,name,name,r,b);
		}else if(r.is_next_char_block_open()){
			r.unread_last_char();
			e=new def_data(this,name,name,r,b);
		}else if(r.is_next_char_struct_open()){
			e=new def_table(this,name,name,r,b);
		}else{
			e=new def_const(this,name,name,r,b);
		}
	}
	@Override public void binary_to(xbin x){
		e.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
//		x.tag("def");
		x.p(name);
//		x.tage("def");
		x.p(ws_after_name);
		e.source_to(x);
	}
}