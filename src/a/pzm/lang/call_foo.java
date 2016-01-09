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
		mark_end_of_source(r);
		ws_after_expression_closed=r.next_empty_space();
		loop_code=statement.read(this,r);
		mark_end_of_source_from(loop_code);
	}
	@Override public void binary_to(xbin x){
		final String table_name=arguments.get(0).token;
		final def_table tbl=(def_table)x.toc.get("table "+table_name);
		String ref_to_register=null;
		if(tbl==null){
			ref_to_register=x.register_for_alias(table_name);
			if(ref_to_register==null)
				throw new compiler_error(this,"table not found",table_name);
		}
		final ArrayList<expression>args;
		final ArrayList<String>allocated_registers=new ArrayList<>();
		final ArrayList<String>aliases=new ArrayList<>();
		if(arguments.size()==1){//select *
			args=new ArrayList<>();
			args.addAll(arguments);
			for(def_table_col col:tbl.arguments){
				if(x.is_register_alias_exists(col.token)){
					throw new compiler_error(this,"var '"+col.token+"' already exists",x.register_aliases.toString());
				}
				final String reg=x.allocate_register(this);
				allocated_registers.add(reg);
				x.alias_register(col.token,reg);
				aliases.add(col.token);
				final expression e=new expression(this,col.token);
				args.add(e);
			}
		}else{
			args=arguments;
			if(args.size()-1!=tbl.arguments.size()) throw new Error("argument count does not match table: "+table_name);
		}
//		args.subList(1,args.size()).forEach(e->parent_statement.declarations.add(e.token));
//		pl("foo "+parent_statement.declarations.toString());
		final expression rd=args.get(0);
		final String ra=x.allocate_register(this);
		allocated_registers.add(ra);
		final int rai=register_index(ra);
		final String rc=x.allocate_register(this);
		final int rci=register_index(rc);
		allocated_registers.add(rc);
		if(ref_to_register==null){
			x.write(0|0x0000|(rai&63)<<14);//li(a dots)
			x.linker_add_li(rd.token);
			x.write(0);
		}else{
			final int rdi=x.register_index_for_alias(this,ref_to_register);
			x.write(0|0x00e0|(rai&63)<<8|(rdi&63)<<14);//tx(a )			
		}
		
		x.write(0|0x00c0|(rai&63)<<8|(rci&63)<<14);//ldc(c a)
		x.write(0|0x0100|(0&63)<<8|(rci&63)<<14);//lp(c)
		for(expression e:args.subList(1,args.size())){
			final int regi=x.register_index_for_alias(this,e.token);
			x.write(0|0x00c0|(rai&63)<<8|(regi&63)<<14);//ldc(c regi)
		}
		loop_code.binary_to(x);
		x.write(4);//nxt
		aliases.forEach(e->x.unalias_register(this,e));
		allocated_registers.forEach(e->x.free_register(e));
	}
	
	private int register_index(String reg_a){
		return reg_a.charAt(0)-'a';
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