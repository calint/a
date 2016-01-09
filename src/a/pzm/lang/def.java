package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class def extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name;
	final private String ws_before_name;
	final private statement e;
	public def(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
		ws_before_name=r.next_empty_space();
		r.set_location_in_source();
		name=r.next_token();
		ws_after_name=r.next_empty_space();
		if(r.is_next_char_expression_open()){
			r.set_location_in_source();
			e=new def_func(this,null,r,name);
		}else if(r.is_next_char_block_open()){
			r.unread_last_char();
			r.set_location_in_source();
			e=new def_data(this,null,r,name);
		}else if(r.is_next_char_struct_open()){
			r.set_location_in_source();
			e=new def_table(this,null,r,name);
		}else{
			r.set_location_in_source();
			e=new def_const(this,name,name,r,this);
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