package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class def_data extends statement{
	private static final long serialVersionUID=1;
	final private statement data;
	final private String name;
	public def_data(statement parent,LinkedHashMap<String,String>annot,reader r,String name)throws Throwable{
		super(parent,annot);
		this.name=name;
		data=statement.read(this,r);
		mark_end_of_source(r);
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