package a.pzm.lang;

import a.pzm.lang.reader.token;
import b.xwriter;

final public class var extends statement{
	public var(statement parent,annotations annot,token vartkn,reader r){
		super(parent,annot,vartkn);
		identtkn=r.next_token();
		if(r.is_next_char_assign()){// ie  tick=3
			mark_end_of_source(r);
			r.set_location_in_source();
			initial_value_expression=expression.read(parent,new annotations(parent,r),identtkn.name,r);
			mark_end_of_source_from(initial_value_expression);
			return;
		}
		initial_value_expression=null;
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		identtkn.to(x);
		if(initial_value_expression!=null){
			x.p("=");
			initial_value_expression.source_to(x);
		}
	}
	@Override public void binary_to(xbin x){
		x.vspc().alloc_var(this,identtkn.name);
		parent_statement().vars_add(identtkn.name);
		if(initial_value_expression!=null)initial_value_expression.binary_to(x);
	}
	final private token identtkn;
	final private expression initial_value_expression;
	private static final long serialVersionUID=1;
}