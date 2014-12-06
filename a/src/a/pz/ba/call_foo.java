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
		loop_code=new block(this,"b",r,block.no_declarations,b);
	}
	@Override public void binary_to(xbin x){
		final String table_name=arguments.get(0).token;
		final def_struct dt=(def_struct)x.toc.get("struct "+table_name);
		if(dt==null) throw new Error("struct not found: "+table_name);

		if(arguments.size()-1!=dt.arguments.size()) throw new Error("argument count does not match table: "+table_name);

		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression rd=arguments.get(0);
		//			if(rd.src.length()!=1) throw new Error("not a register: "+rd.src);
		//			final int rdi=rd.src.charAt(0)-'a';
		//			if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
		//			final int rai=0,znxr=0;
		//			final int i=0x0100|znxr|(rai&15)<<8|(rdi&15)<<12;
		//			x.write(i);

		x.write(0|0x0000|(0&15)<<8|(0&15)<<12);//li(a dots)
		x.link_li(rd.token);
		x.write(0);
		x.write(0|0x00c0|(0&15)<<8|(2&15)<<12);//ldc(c a)
		x.write(0|0x0100|(0&15)<<8|(2&15)<<12);//lp(c)
		for(expression e:arguments.subList(1,arguments.size())){
			final String reg=e.token;
			final int regi=reg.charAt(0)-'a';
			x.write(0|0x00c0|(0&15)<<8|(regi&15)<<12);//ldc(c regi)
		}
		loop_code.binary_to(x);
		x.write(4);//nxt
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