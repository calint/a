package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.xwriter;

final public class call_fow extends statement{
	private static final long serialVersionUID=1;
	final private ArrayList<expression>arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private statement loop_code;
	public call_fow(statement parent,LinkedHashMap<String,String>annot,reader r)throws Throwable{
		super(parent,annot);
		mark_start_of_source(r);
		ws_after_expression_open=r.next_empty_space();
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(this,null,r,null,null);
			arguments.add(arg);
		}
		mark_end_of_source(r);
		ws_after_expression_closed=r.next_empty_space();
//		arguments.forEach(e->declarations.add(e.token));
		loop_code=statement.read(this,r);
		mark_end_of_source_from(loop_code);
	}
	@Override public void binary_to(xbin x){
		final String table_name=arguments.get(0).token;
		final def_table dt=(def_table)x.toc.get("table "+table_name);
		if(dt==null) throw new Error("struct not found: "+table_name);
		final ArrayList<expression>args;
		final ArrayList<String>allocated_registers=new ArrayList<>();
		final ArrayList<String>aliases=new ArrayList<>();
		if(arguments.size()==1){//select *
			args=new ArrayList<>();
			args.addAll(arguments);
			final String struct_name=args.get(0).token;
			final def_table stc=(def_table)x.toc.get("table "+struct_name);
			if(stc==null)throw new compiler_error(this,"struct not declared yet",struct_name);
			for(def_table_col col:stc.arguments){
				x.vspc().alloc_var(col,col.token);
				aliases.add(col.token);
				final expression e=new expression(this,col.token);
				args.add(e);
			}
		}else{
			args=arguments;
			if(args.size()-1!=dt.arguments.size()) throw new Error("argument count does not match table: "+table_name);
		}
//		args.subList(1,args.size()).forEach(e->parent_statement.declarations.add(e.token));
		final String ra=x.alloc_register(this);
		allocated_registers.add(ra);
		final int rai=get_register_index_for_name(ra);
		final String rb=x.alloc_register(this);
		allocated_registers.add(rb);
		final int rbi=get_register_index_for_name(rb);
		final String rc=x.alloc_register(this);
		final int rci=get_register_index_for_name(rc);
		allocated_registers.add(rc);
		x.write(0|0x0000|(rai&63)<<14,this);//li(a dots)
		x.linker_add_li(arguments.get(0).token);
		x.write(0,this);
		x.write(0|0x00c0|(rai&63)<<8|(rci&63)<<14,this);//ldc(c a)
		x.write(0|0x0100|(rci&63)<<14,this);//lp(c)
		x.write(0|0x00e0|(rai&63)<<8|(rbi&63)<<14,this);//tx(b a)
		for(expression e:args.subList(1,args.size())){
			final String reg=e.token;
			final int regi=x.vspc().get_register_index(this,reg);//reg.charAt(0)-'a';
			x.write(0|0x00c0|(rai&63)<<8|(regi&63)<<14,this);//ldc(c regi)
		}
		loop_code.binary_to(x);
		x.write(0|0x00e0|(rbi&63)<<8|(rai&63)<<14,this);//tx(a b)
		for(expression e:args.subList(1,args.size())){
			final String reg=e.token;
			final int regi=x.vspc().get_register_index(this,reg);//reg.charAt(0)-'a';
//			final int regi=reg.charAt(0)-'a';
			x.write(0|0x0040|(rai&63)<<8|(regi&63)<<14,this);//stc(a reg)
		}
		aliases.forEach(e->x.vspc().free_var(this,e));
		allocated_registers.forEach(e->x.free_register(this,e));
		x.write(4,this);//nxt
	}
	
	
	//??
	private int get_register_index_for_name(String reg_a){
		return reg_a.charAt(0)-'a';
	}

	@Override public void source_to(xwriter x){
		x.p("fow");
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