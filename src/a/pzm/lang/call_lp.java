package a.pzm.lang;

import java.util.ArrayList;

import a.pzm.lang.reader.token;
import b.xwriter;

final public class call_lp extends statement{
	final public static int op=0x100;
	public call_lp(statement parent,annotations annot,token tk,reader r)throws Throwable{
		super(parent,annot,tk);
		mark_start_of_source(r);
		mark_end_of_source(r);
		while(true){
			if(r.is_next_char_expression_close())break;
			final expression arg=expression.read(this,new annotations(parent,r),null,r);
			arguments.add(arg);
			mark_end_of_source(r);
		}
		mark_end_of_source(r);
		loop_code=statement.read(this,r);
		mark_end_of_source_from(loop_code);
	}
	@Override public void binary_to(xbin x){
		if(arguments.size()!=1)throw new compiler_error(this,"expected one argument, got "+arguments.size(),"");
		final expression rd=arguments.get(0);
		final int rdi=x.vspc().get_register_index(rd,rd.token.name);
		x.write_op(this,op,0,rdi);
		loop_code.binary_to(x);
		x.write_op(this,4,0,0);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("(");
		for(final expression e:arguments)e.source_to(x);
		x.p(")");
		loop_code.source_to(x);
	}
	final private ArrayList<expression>arguments=new ArrayList<>();
	final private statement loop_code;
	private static final long serialVersionUID=1;
}