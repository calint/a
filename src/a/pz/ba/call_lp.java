package a.pz.ba;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

final public class call_lp extends statement{
	private static final long serialVersionUID=1;
	final private ArrayList<expression> arguments=new ArrayList<>();
	final private String ws_after_expression_open,ws_after_expression_closed;
	final private block loop_code;
	public call_lp(a pt,String nm,LinkedHashMap<String,String> annotations,reader r){
		super(pt,nm,annotations,"",r);
		ws_after_expression_open=r.next_empty_space();
		int i=0;
		while(true){
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(this,"e"+i++,no_annotations,r);
			arguments.add(arg);
		}
		ws_after_expression_closed=r.next_empty_space();
		//			if(!r.is_next_char_block_open())throw new Error("expected { for loop code");
		loop_code=new block(this,"b",r);
	}
	@Override public void binary_to(xbin x){
		//   znxr|op|((rai&15)<<8)|((rdi&15)<<12);
		final expression rd=arguments.get(0);
		if(rd.token.length()!=1) throw new Error("not a register: "+rd.token);
		final int rdi=rd.token.charAt(0)-'a';
		if(rdi<0||rdi>15) throw new Error("destination registers 'a' through 'p' available");
		final int rai=0,znxr=0;
		final int i=0x0100|znxr|(rai&15)<<8|(rdi&15)<<12;
		x.write(i);
		loop_code.binary_to(x);
		x.write(4);//nxt
	}
	@Override public void source_to(xwriter x){
		x.tag("ac").p("lp").tage("ac");
		super.source_to(x);
		x.p("(");
		x.p(ws_after_expression_open);
		arguments.forEach(e->e.source_to(x));
		x.p(")");
		x.p(ws_after_expression_closed);
		loop_code.source_to(x);
	}
}