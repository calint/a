package a.pzm.lang;

import java.util.ArrayList;

import a.pzm.lang.reader.token;
import b.xwriter;

final public class call_foo extends statement{
	public call_foo(statement parent,annotations annot,token tk,reader r)throws Throwable{
		super(parent,annot,tk);
		mark_start_of_source(r);
		ws_after_expression_open=r.next_empty_space();
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_expression_close())break;
			final expression arg=expression.read(this,new annotations(parent,r),null,r);
			arguments.add(arg);
		}
		if(arguments.size()<1)throw new compiler_error(this,"expected at least one argument","");
		mark_end_of_source(r);
		ws_after_expression_closed=r.next_empty_space();
		loop_code=statement.read(this,r);
		mark_end_of_source_from(loop_code);
	}
	@Override public void binary_to(xbin x){
		String table_name=arguments.get(0).token.name;
		def_table tbl=(def_table)x.toc.get("table "+table_name);
		boolean ispointer=false;
		if(tbl==null){
			if(annotations!=null && annotations.count()!=0){
				table_name=annotations.get(0).name;
				tbl=(def_table)x.toc.get("table "+table_name);
				if(tbl==null)
					throw new compiler_error(this,"table '"+table_name+"' from annotation not found","");
				ispointer=true;
			}else{
//				final String funcs=x.toc.keySet().stream().filter(s->s.startsWith("table ")).map(s->s.substring("table ".length())).collect(Collectors.toList()).toString();
				final ArrayList<String>ls=new ArrayList<>();
				for(final String s:x.toc.keySet()){
					if(!s.startsWith("table "))continue;
					ls.add(s.substring("table ".length()));
				}
				final String funcs=ls.toString();
				throw new compiler_error(this,"table '"+table_name+"' not found",funcs);
			}
		}
		x.push_block();
		final ArrayList<expression>args;
		if(arguments.size()==1){//select *
			args=new ArrayList<>();
			args.addAll(arguments);
//			tbl.arguments.forEach(col -> {
//				final String col_name = col.token;
//				x.vspc().alloc_var(this, col_name);
////				allocated_vars.add(col_name);
//				final expression e = new expression(this, col_name);
//				args.add(e);
//			});
			for(final def_table_col col:tbl.arguments){
				final String col_name=col.token.name;
				x.vspc().alloc_var(this,col_name);
//				allocated_vars.add(col_name);
				final expression e=new expression(this,col_name);
				args.add(e);
			}
		}else{
			args=arguments;
			if(args.size()-1!=tbl.arguments.size()) throw new Error("argument count does not match table: "+table_name);
		}
		final int rai=x.vspc().alloc_var(this,"$ra");
		final int rci=x.vspc().alloc_var(this,"$rc");
		if(!ispointer){
			x.write_op(this,call_li.op,0,rai);
			x.linker_add_li(table_name);
			x.write(0,this);
		}else{
			final int ri=x.vspc().get_register_index(this,arguments.get(0).token.name);
			x.write_op(this,call_tx.op,ri,rai);
		}
		x.write_op(this,call_ldc.op,rai,rci);
		x.write_op(this,call_lp.op,0,rci);
		for(expression e:args.subList(1,args.size())){// read record into registers
			final int rdi=x.vspc().get_register_index(this,e.token.name);
			x.write_op(this,call_ldc.op,rai,rdi);
		}
		loop_code.binary_to(x);
		x.write_op(this,4,0,0);//nxt
		x.pop(this);
	}
	
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("(");
		x.p(ws_after_expression_open);
//		x.tag("dr");
		arguments.get(0).source_to(x);
//		x.tage("dr");
//		arguments.subList(1,arguments.size()).forEach(e -> e.source_to(x));
		for(final expression e:arguments.subList(1,arguments.size())){
			e.source_to(x);
		}
		x.p(")");
		x.p(ws_after_expression_closed);
		loop_code.source_to(x);
	}
	final private ArrayList<expression>arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private statement loop_code;
	private static final long serialVersionUID=1;
}