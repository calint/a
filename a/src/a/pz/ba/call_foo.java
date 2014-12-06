package a.pz.ba;

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
		if(dt==null) throw new Error("struct not found: "+table_name);
		final ArrayList<expression>args;
		final ArrayList<String>allocated_registers=new ArrayList<>();
		x.allocate_register(this,"a");
		x.allocate_register(this,"b");
		x.allocate_register(this,"c");
		allocated_registers.add("a");
		allocated_registers.add("b");
		allocated_registers.add("c");
		
//		final String reg_a=x.allocate_register(this,"a");
//		final String reg_b=x.allocate_register(this,"b");
//		final String reg_c=x.allocate_register(this,"c");
//		allocated_registers.add(reg_a);
//		allocated_registers.add(reg_b);
//		allocated_registers.add(reg_c);
		if(arguments.size()==1){//select *
			args=new ArrayList<>();
			args.addAll(arguments);
			final String struct_name=args.get(0).token;
			final def_struct stc=(def_struct)x.toc.get("struct "+struct_name);
			if(stc==null)throw new compiler_error(this,"struct not declared yet",struct_name);
			int r='d';
			for(def_struct_field col:stc.arguments){
				final String rn=(char)r+++"";
				x.allocate_register(this,rn);
				allocated_registers.add(rn);
				args.add(new expression(rn));
//				x.decl_register_alias(rn,col.token);
			}
		}else{
			args=arguments;
			if(args.size()-1!=dt.arguments.size()) throw new Error("argument count does not match table: "+table_name);
		}
		args.subList(1,args.size()).forEach(e->blk.declarations.add(e.token));
		final expression rd=args.get(0);
//		final int rai=register_index(reg_a);
////		final int rbi=register_index(reg_b);
//		final int rci=register_index(reg_c);
		final int rai=register_index("a");
		final int rci=register_index("c");
		x.write(0|0x0000|(rai&15)<<12);//li(a dots)
		x.link_li(rd.token);
		x.write(0);
		x.write(0|0x00c0|(0&15)<<8|(rci&15)<<12);//ldc(c a)
		x.write(0|0x0100|(0&15)<<8|(rci&15)<<12);//lp(c)
		for(expression e:args.subList(1,args.size())){
			final String reg=e.token;
			final int regi=reg.charAt(0)-'a';
			x.write(0|0x00c0|(0&15)<<8|(regi&15)<<12);//ldc(c regi)
		}
		loop_code.binary_to(x);
		x.write(4);//nxt
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