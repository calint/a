package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class call_foo extends statement{
	private static final long serialVersionUID=1;
	final private ArrayList<expression>arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private statement loop_code;
	public call_foo(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
		mark_start_of_source(r);
		ws_after_expression_open=r.next_empty_space();
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_expression_close())break;
			final expression arg=new expression(this,null,r,"",(String)null);
			arguments.add(arg);
		}
		token=arguments.get(0).token;
		mark_end_of_source(r);
		ws_after_expression_closed=r.next_empty_space();
		loop_code=statement.read(this,r);
		mark_end_of_source_from(loop_code);
	}
	@Override public void binary_to(xbin x){
		final String table_name=arguments.get(0).token;
		final def_table tbl=(def_table)x.toc.get("table "+table_name);
		if(tbl==null)throw new compiler_error(this,"table not found",table_name);
		final ArrayList<expression>args;
		x.push_block();
		final ArrayList<String>allocated_vars=new ArrayList<>();
		if(arguments.size()==1){//select *
			args=new ArrayList<>();
			args.addAll(arguments);
			tbl.arguments.forEach(col->{
				final String col_name=col.token;
				x.vspc().alloc_var(this,col_name);
				allocated_vars.add(col_name);
				final expression e=new expression(this,col_name);
				args.add(e);				
			});
		}else{
			args=arguments;
			if(args.size()-1!=tbl.arguments.size()) throw new Error("argument count does not match table: "+table_name);
		}
		final expression rd=args.get(0);
		
		final int rai=x.vspc().alloc_var(this,"$ra");
		allocated_vars.add("$ra");
		final int rci=x.vspc().alloc_var(this,"$rc");
		allocated_vars.add("$rc");
		
		x.write(0|0x0000|(rai&63)<<14,this);//li(a dots)
		x.linker_add_li(rd.token);
		x.write(0,this);
		
		x.write(0|0x00c0|(rai&63)<<8|(rci&63)<<14,this);//ldc(c a)
		x.write(0|0x0100|(0&63)<<8|(rci&63)<<14,this);//lp(c)
		for(expression e:args.subList(1,args.size())){
			final int regi=x.vspc().get_register_index(this,e.token);
			x.write(0|0x00c0|(rai&63)<<8|(regi&63)<<14,this);//ldc(c regi)
		}
		loop_code.binary_to(x);
		x.write(4,this);//nxt

		allocated_vars.forEach(e->x.vspc().free_var(this,e));
		x.pop(this);
	}
	
	@Override public void source_to(xwriter x){
		x.p("foo");
		super.source_to(x);
		x.p("(");
		x.p(ws_after_expression_open);
//		x.tag("dr");
		arguments.get(0).source_to(x);
//		x.tage("dr");
		arguments.subList(1,arguments.size()).forEach(e->e.source_to(x));
		x.p(")");
		x.p(ws_after_expression_closed);
		loop_code.source_to(x);
	}
}