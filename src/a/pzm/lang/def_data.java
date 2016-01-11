package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class def_data extends def{
	final private String ws_before_name,name,ws_after_name;
	final private statement data;
	public def_data(statement parent,LinkedHashMap<String,String>annot,reader r,String name,String ws_before_name,String ws_after_name)throws Throwable{
		super(parent,annot);
		this.ws_before_name=ws_before_name;
		mark_start_of_source(r);
		this.name=name;
		mark_end_of_source(r);
		this.ws_after_name=ws_after_name;
		data=statement.read(this,r);
		mark_end_of_source(r);
		r.toc.put("data "+name,this);
	}
	@Override public void binary_to(xbin x){
		x.def(name);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("def").p(ws_before_name).p(name).p(ws_after_name);
		data.source_to(x);
	}
	private static final long serialVersionUID=1;
}