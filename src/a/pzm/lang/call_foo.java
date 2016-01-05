package a.pzm.lang;

import static b.b.pl;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

final public class call_foo extends statement{
	private static final long serialVersionUID=1;
	final private ArrayList<expression> arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private block loop_code;
	public call_foo(a pt,String nm,LinkedHashMap<String,String> annotations,reader r,block b){
		super(pt,nm,annotations,"",r,b);
		ws_after_expression_open=r.next_empty_space();
		int i=0;
		while(true){
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(this,"e"+i++,no_annotations,r,b);
			arguments.add(arg);
		}
		ws_after_expression_closed=r.next_empty_space();
		//			if(!r.is_next_char_block_open())throw new Error("expected { for loop code");
		final ArrayList<String>declarations=new ArrayList<>();
		loop_code=new block(this,"b",r,declarations,b);
	}
	@Override public void binary_to(xbin x){
		final String table_name=arguments.get(0).token;
		final def_struct dt=(def_struct)x.toc.get("struct "+table_name);
		String ref_to_register=null;
		if(dt==null){
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
			final String struct_name=args.get(0).token;
			final def_struct stc=(def_struct)x.toc.get("struct "+struct_name);
			if(stc==null)throw new compiler_error(this,"struct not declared yet",struct_name);
			for(def_struct_field col:stc.arguments){
				final String reg=x.allocate_register(this);
				allocated_registers.add(reg);
				x.alias_register(col.token,reg);
				aliases.add(col.token);
				final expression e=new expression(col.token);
				args.add(e);
			}
		}else{
			args=arguments;
			if(args.size()-1!=dt.arguments.size()) throw new Error("argument count does not match table: "+table_name);
		}
		args.subList(1,args.size()).forEach(e->blk.declarations.add(e.token));
		pl("foo "+blk.declarations.toString());
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
			x.write(0|0x00e0|(rai&63)<<8|(rdi&63)<<14);//li(a dots)			
		}
		
		x.write(0|0x00c0|(rai&63)<<8|(rci&63)<<14);//ldc(c a)
		x.write(0|0x0100|(0&63)<<8|(rci&63)<<14);//lp(c)
		for(expression e:args.subList(1,args.size())){
			final int regi=x.register_index_for_alias(this,e.token);//reg.charAt(0)-'a';
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
		x.tag("dr");
		arguments.get(0).source_to(x);
		x.tage("dr");
		arguments.subList(1,arguments.size()).forEach(e->e.source_to(x));
		x.p(")");
		x.p(ws_after_expression_closed);
		loop_code.source_to(x);
	}
}