package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class call_lp extends statement{
	private static final long serialVersionUID=1;
	final public static int op=0x100;
	final private ArrayList<expression> arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private statement loop_code;
	public call_lp(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
		mark_start_of_source(r);
		mark_end_of_source(r);
		ws_after_expression_open=r.next_empty_space();
		while(true){
			if(r.is_next_char_expression_close())break;
			final expression arg=new expression(this,null,r,null,null);
			arguments.add(arg);
			mark_end_of_source(r);
		}
		mark_end_of_source(r);
		ws_after_expression_closed=r.next_empty_space();
		loop_code=statement.read(this,r);
		mark_end_of_source_from(loop_code);
	}
	@Override public void binary_to(xbin x){
		if(arguments.size()!=1)throw new compiler_error(this,"expected one argument, got "+arguments.size(),"");
//		ensure_arg_count(1);
		final expression rd=arguments.get(0);
		final int rdi=x.vspc().get_register_index(rd,rd.token);
		x.write_op(this,op,0,rdi);
		loop_code.binary_to(x);
		x.write_op(this,4,0,0);
//		x.write(4,this);//nxt
	}
	@Override public void source_to(xwriter x){
		x.p("lp");
		super.source_to(x);
		x.p("(");
		x.p(ws_after_expression_open);
//		arguments.forEach(e -> e.source_to(x));
		for(final expression e:arguments){
			e.source_to(x);
		}
		x.p(")");
		x.p(ws_after_expression_closed);
		loop_code.source_to(x);
	}
}