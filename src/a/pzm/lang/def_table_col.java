package a.pzm.lang;

import java.util.LinkedHashMap;

final public class def_table_col extends statement{
	private static final long serialVersionUID=1;
	private String ws_after_name;
	public def_table_col(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
		token=r.next_token();
		if(token.length()==0)
			throw new compiler_error(this,"unexpected empty token","");
		mark_end_of_source(r);
		ws_after_name=r.next_empty_space();
//		r.toc.put("table_column "+token,this);
	}
}