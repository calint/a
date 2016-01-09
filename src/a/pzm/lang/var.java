package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class var extends statement{
	private static final long serialVersionUID=1;
	final private String var_name,ws_after_name,ws_after_assign;
	final private expression initial_value_expression;
	final private String ws_left;
	public var(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot);
		mark_start_of_source(r);
		ws_left=r.next_empty_space();
//		r.set_location_in_source();
//		mark_start_of_source(r);
		var_name=r.next_token();
		mark_end_of_source(r);
		ws_after_name=r.next_empty_space();
		if(r.is_next_char_assign()){
			ws_after_assign=r.next_empty_space();
			mark_end_of_source(r);
			r.set_location_in_source();
			initial_value_expression=new expression(this,null,r,var_name,null);
			mark_end_of_source_from(initial_value_expression);
		}else{
			ws_after_assign="";
			initial_value_expression=null;
		}
//		mark_end_of_source(r);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("var").p(ws_left).p(var_name).p(ws_after_name);
		if(initial_value_expression!=null){
			x.p("=").p(ws_after_assign);
			initial_value_expression.source_to(x);
		}
	}
	@Override public void binary_to(xbin x){
		if(x.is_register_alias_exists(var_name)){
			throw new compiler_error(this,"var '"+var_name+"' already used",x.register_aliases.keySet().toString());
		}
		final String reg=x.allocate_register(this);
		x.alias_register(var_name,reg);
		parent_statement().vars_add(var_name);
		if(initial_value_expression!=null){
			if(x.is_register_alias_exists(initial_value_expression.token)){
				final int rai=x.register_index_for_alias(this,initial_value_expression.token);
				final int rdi=x.register_index_for_alias(this,var_name);
				x.write(0|0x00e0|(rai&63)<<8|(rdi&63)<<14);//tx(rai rdi)
				return;
			}
			final int rai=x.register_index_for_alias(this,var_name);
			x.write(0|0x0000|(rai&63)<<14);//li(a initial_expression)
			x.at_pre_link_evaluate(initial_value_expression);
			x.write(0);
		}
	}
}