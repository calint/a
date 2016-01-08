package a.pzm.lang;

import java.util.LinkedHashMap;

import b.a;
import b.xwriter;

final public class def_var extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name,ws_after_assign;
	final private expression initial_value_expression;
	public def_var(a pt,String nm,statement parent_statement,LinkedHashMap<String,String>annotations,reader r){
		super(pt,nm,annotations,"var",r,parent_statement);
		r.set_location_in_source();
		mark_start_of_source(r);
		name=r.next_token();
		mark_end_of_source(r);
		ws_after_name=r.next_empty_space();
		if(r.is_next_char_assign()){
			ws_after_assign=r.next_empty_space();
			r.set_location_in_source();
			initial_value_expression=new expression(this,name,this,r,name);
		}else{
			ws_after_assign="";
			initial_value_expression=null;
		}
	}
//	public var(a pt,String nm,reader r,statement parent_statement){
//		this(pt,nm,parent_statement,new LinkedHashMap<>(),r);
//	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p(name).p(ws_after_name);
		if(initial_value_expression!=null){
			x.p("=").p(ws_after_assign);
			initial_value_expression.source_to(x);
		}
	}
	@Override public void binary_to(xbin x){
		final String reg=x.allocate_register(this);
		x.alias_register(name,reg);
		parent_statement.vars_add(name);
		if(initial_value_expression!=null){
			final int rai=x.register_index_for_alias(this,name);
			x.write(0|0x0000|(rai&63)<<14);//li(a initial_expression)
			x.at_pre_link_evaluate(initial_value_expression);
			x.write(0);
		}
	}
}