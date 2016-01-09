package a.pzm.lang;

import java.util.LinkedHashMap;

public class def extends statement{
	public static def read(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		final String ws_before_name=r.next_empty_space();
		r.set_location_in_source();
		final String name=r.next_token();
		final String ws_after_name=r.next_empty_space();
		if(r.is_next_char_expression_open()){
			r.set_location_in_source();
			final def e=new def_func(parent,annot,r,name,ws_before_name,ws_after_name);
			return e;
		}else if(r.is_next_char_block_open()){
			r.unread_last_char();
			r.set_location_in_source();
			final def e=new def_data(parent,annot,r,name,ws_before_name,ws_after_name);
			return e;
		}else if(r.is_next_char_struct_open()){
			r.set_location_in_source();
			final def e=new def_table(parent,annot,r,name,ws_before_name,ws_after_name);
			return e;
		}else{
			r.set_location_in_source();
			final def e=new def_const(parent,annot,r,name);
			return e;
		}
	}
	public def(statement parent,LinkedHashMap<String,String>annot){
		super(parent,annot);
	}
	private static final long serialVersionUID=1;
//	final private String name,ws_after_name;
//	final private String ws_before_name;
//	final private statement e;
//	public def(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
//		super(parent,annot);
//		ws_before_name=r.next_empty_space();
//		r.set_location_in_source();
//		name=r.next_token();
//		ws_after_name=r.next_empty_space();
//		if(r.is_next_char_expression_open()){
//			r.set_location_in_source();
//			e=new def_func(this,null,r,name);
//		}else if(r.is_next_char_block_open()){
//			r.unread_last_char();
//			r.set_location_in_source();
//			e=new def_data(this,null,r,name);
//		}else if(r.is_next_char_struct_open()){
//			r.set_location_in_source();
//			e=new def_table(this,null,r,name);
//		}else{
//			r.set_location_in_source();
//			e=new def_const(this,null,r,name);
//		}
//	}
//	@Override public void binary_to(xbin x){}
//	@Override public void source_to(xwriter x){}
}