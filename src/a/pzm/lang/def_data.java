package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.xwriter;

final public class def_data extends def{
	public def_data(statement parent,annotations annot,token deftkn,token tkn,reader r)throws Throwable{
		super(parent,annot,deftkn);
		nametoken=tkn;
		mark_start_of_source(r);
		mark_end_of_source(r);
		data=statement.read(this,r);
		mark_end_of_source(r);
		r.toc.put("data "+tkn.name,this);
	}
	@Override public void binary_to(xbin x){
		x.def(token.name);
		data.binary_to(x);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		nametoken.to(x);
		data.source_to(x);
	}
	final private token nametoken;
	final private statement data;
	private static final long serialVersionUID=1;
}