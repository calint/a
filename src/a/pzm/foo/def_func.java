package a.pzm.foo;

import java.util.ArrayList;
import b.a;
import b.xwriter;

final public class def_func extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_expr_close;
	final ArrayList<expression> arguments=new ArrayList<>();
	final block function_code;
	public def_func(a pt,String nm,String name,reader r,block b){
		super(pt,nm,no_annotations,"",r,b);
		this.name=name;
		int i=0;
		while(true){
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(this,""+i++,no_annotations,r,b);
			arguments.add(arg);
		}
		ws_after_expr_close=r.next_empty_space();
		final ArrayList<String>declarations=new ArrayList<>();
		arguments.forEach(e->declarations.add(e.token));
		function_code=new block(this,"c",r,declarations,b);
		r.toc.put("func "+name,this);
	}
	@Override public void binary_to(xbin x){
		return;
//		x.def(name,this);
//		function_code.binary_to(x);
//		x.write(8);//ret // if last instr 4 set last instr 4+8
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
//		x.p(name);
		x.p("(");
		arguments.forEach(e->e.source_to(x));
		x.p(")").p(ws_after_expr_close);
		function_code.source_to(x);
	}
}