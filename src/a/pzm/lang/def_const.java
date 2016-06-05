package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class def_const extends def{
	private static final long serialVersionUID=1;
	final private String ws_before_name,ws_after_name;
	final private String ws_trailing;
	final expression expr;
	public def_const(statement parent,LinkedHashMap<String,String>annot,reader r,String name,String ws_before_name,String ws_after_name)throws Throwable{
		super(parent,annot);
		nm(name);
		mark_start_of_source(r);
		this.ws_before_name=ws_before_name;
		token=name;
		mark_end_of_source(r);
		this.ws_after_name=ws_after_name;
		expr=expression.read(this,r.read_annotation(),null,r);
		mark_end_of_source_from(expr);
		ws_trailing=r.next_empty_space();
		r.toc.put("const "+name,this);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("def").p(ws_before_name).p(token).p(ws_after_name);
		expr.source_to(x);
		x.p(ws_trailing);
	}
}