package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.xwriter;

final public class def_const extends def{
	public def_const(statement parent,annotations annot,token deftkn,token tkn,reader r)throws Throwable{
		super(parent,annot,deftkn);
		nm(tkn.name);
		nametkn=tkn;
		mark_start_of_source(r);
		mark_end_of_source(r);
		expr=expression.read(this,new annotations(parent,r),null,r);
		mark_end_of_source_from(expr);
		r.toc.put("const "+tkn.name,this);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		nametkn.to(x);
		expr.source_to(x);
	}
	final private token nametkn;
	final private expression expr;
	private static final long serialVersionUID=1;
}