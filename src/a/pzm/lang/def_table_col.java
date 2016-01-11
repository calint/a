package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class def_table_col extends statement{
	private static final long serialVersionUID=1;
	private String ws_leading,ws_trailing;
	public def_table_col(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
		ws_leading=r.next_empty_space();
		mark_start_of_source(r);
		token=r.next_token();
		if(token.length()==0)
			throw new compiler_error(this,"unexpected empty token","");
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
//		r.toc.put("table_column "+token,this);
	}
	@Override public void source_to(xwriter x){
		x.p(ws_leading).p(token).p(ws_trailing);
	}
}