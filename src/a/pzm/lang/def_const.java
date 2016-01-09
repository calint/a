package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class def_const extends def{
	private static final long serialVersionUID=1;
	final private String ws_trailing;
	final expression expr;
	public def_const(statement parent,LinkedHashMap<String,String>annot,reader r,String name){
		super(parent,annot);
		expr=new expression(this,null,r,null,null);
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
		r.toc.put("const "+name,this);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		expr.source_to(x);
		x.p(ws_trailing);
	}
}