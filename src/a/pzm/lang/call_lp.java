package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class call_lp extends statement{
	private static final long serialVersionUID=1;
	final private ArrayList<expression> arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private statement loop_code;
	public call_lp(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
//		if(!r.is_next_char_expression_open()){
//			throw new compiler_error(this,"expected '(' for 'lp' arguments","");
//		}
		mark_start_of_source(r);
		mark_end_of_source(r);
		ws_after_expression_open=r.next_empty_space();
//		int i=0;
		while(true){
			if(r.is_next_char_expression_close())break;
			final expression arg=new expression(this,null,r,null,null);
			arguments.add(arg);
			mark_end_of_source(r);
		}
		mark_end_of_source(r);
		ws_after_expression_closed=r.next_empty_space();
		//			if(!r.is_next_char_block_open())throw new Error("expected { for loop code");
		loop_code=statement.read(this,r);//? this parent
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression rd=arguments.get(0);
		final int rdi=x.register_index_for_alias(this,rd.token);
		final int rai=0,znxr=0;
		final int i=0x0100|znxr|(rai&63)<<8|(rdi&63)<<14;
		x.write(i);
		loop_code.binary_to(x);
		x.write(4);//nxt
	}
	@Override public void source_to(xwriter x){
		x.p("lp");
		super.source_to(x);
		x.p("(");
		x.p(ws_after_expression_open);
		arguments.forEach(e->e.source_to(x));
		x.p(")");
		x.p(ws_after_expression_closed);
		loop_code.source_to(x);
	}
}