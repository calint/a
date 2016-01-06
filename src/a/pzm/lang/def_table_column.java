package a.pzm.lang;

import b.a;

final public class def_table_column extends statement{
	private static final long serialVersionUID=1;
	public def_table_column(a pt,String nm,reader r,block b){
		super(pt,nm,no_annotations,r.next_token(),r,b);
		r.toc.put("table_column "+token,this);
	}
}