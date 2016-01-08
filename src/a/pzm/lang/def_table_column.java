package a.pzm.lang;

import b.a;

final public class def_table_column extends statement{
	private static final long serialVersionUID=1;
	public def_table_column(a pt,String nm,reader r,statement b){
		super(pt,nm,null,r.next_token(),r,b);
		mark_end_of_source(r);
		r.toc.put("table_column "+token,this);
	}
}