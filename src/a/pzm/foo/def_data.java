package a.pzm.foo;

import b.a;
import b.xwriter;

final public class def_data extends statement{
	private static final long serialVersionUID=1;
	final private block data;
	final private String name;
	public def_data(a pt,String nm,String name,reader r,block b){
		super(pt,nm,no_annotations,"",r,b);
		this.name=name;
		data=new block(this,"d",r,block.no_declarations,b);
		r.toc.put("data "+name,this);
	}
	@Override public void binary_to(xbin x){
		x.data(name,this);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		data.source_to(x);
	}
}