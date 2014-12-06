package a.pz.ba;

import b.a;

final public class def_struct_field extends statement{
	private static final long serialVersionUID=1;
	public def_struct_field(a pt,String nm,reader r,block b){
		super(pt,nm,no_annotations,r.next_token(),r,b);
		r.toc.put("structfield "+token,this);
	}
}