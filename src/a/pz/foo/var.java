package a.pz.foo;

import b.a;
import b.xwriter;

final public class var extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_trailing;
	public var(a pt,String nm,reader r,block b){
		super(pt,nm,no_annotations,"var",r,b);
		name=r.next_token();
		ws_trailing=r.next_empty_space();
		if(b.declarations.contains(name))
			throw new compiler_error(this,"register already declared",name);
		b.declarations.add(name);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p(name).p(ws_trailing);
	}
}