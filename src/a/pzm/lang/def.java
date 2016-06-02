package a.pzm.lang;

import java.util.LinkedHashMap;

public class def extends statement{
	public static def read(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		final String ws_before_name=r.next_empty_space();
		final String name=r.next_token();
		final String ws_after_name=r.next_empty_space();
		if(r.is_next_char_expression_open())return new def_func(parent,annot,r,name,ws_before_name,ws_after_name);
		if(r.is_next_char_block_open()){
			r.unread_last_char();
			r.set_location_in_source();
			return new def_data(parent,annot,r,name,ws_before_name,ws_after_name);
		}
		if(r.is_next_char_struct_open())return new def_table(parent,annot,r,name,ws_before_name,ws_after_name);
		return new def_const(parent,annot,r,name,ws_before_name,ws_after_name);
	}
	public def(statement parent,LinkedHashMap<String,String>annot){super(parent,annot);}
	private static final long serialVersionUID=1;
}