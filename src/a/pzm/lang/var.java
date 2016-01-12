package a.pzm.lang;

import java.util.LinkedHashMap;

import b.xwriter;

final public class var extends statement{
	private static final long serialVersionUID=1;
	final private String ws_after_name,ws_after_assign;
	final private expression initial_value_expression;
	final private String ws_left;
	public var(statement parent,LinkedHashMap<String,String>annot,reader r){
		super(parent,annot);
		mark_start_of_source(r);
		ws_left=r.next_empty_space();
		token=r.next_token();
		mark_end_of_source(r);
		ws_after_name=r.next_empty_space();
		if(r.is_next_char_assign()){
			ws_after_assign=r.next_empty_space();
			mark_end_of_source(r);
			r.set_location_in_source();
			initial_value_expression=new expression(this,null,r,token,null);
			mark_end_of_source_from(initial_value_expression);
		}else{
			ws_after_assign="";
			initial_value_expression=null;
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("var").p(ws_left).p(token).p(ws_after_name);
		if(initial_value_expression!=null){
			x.p("=").p(ws_after_assign);
			initial_value_expression.source_to(x);
		}
	}
	@Override public void binary_to(xbin x){
		x.vspc().alloc_var(this,token);
		parent_statement().vars_add(token);
		if(initial_value_expression!=null){
//			if(x.vspc().is_declared(initial_value_expression.token)){
//				final int rai=x.vspc().get_register_index(this,initial_value_expression.token);
//				final int rdi=x.vspc().get_register_index(this,token);
//				x.write_op(this,call_tx.op,rai,rdi);
////				x.write(0|0x00e0|(rai&63)<<8|(rdi&63)<<14,this);//tx(rai rdi)
//				return;
//			}
//			final int rai=x.vspc().get_register_index(this,token);
////			x.write(0|0x0000|(rai&63)<<14,this);//li(a initial_expression)
//			x.write_op(this,call_li.op,0,rai);
//			x.add_at_pre_link_evaluate(initial_value_expression);
//			x.write(0,initial_value_expression);
			
			initial_value_expression.binary_to(x);
		}
	}
}