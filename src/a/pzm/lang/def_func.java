package a.pzm.lang;

import java.util.ArrayList;
import b.a;
import b.xwriter;

final public class def_func extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_expr_close;
	final ArrayList<expression>arguments=new ArrayList<>();
	final statement function_code;
	public def_func(a pt,String nm,String name,reader r,statement b){
		super(pt,nm,no_annotations,"",r,b);
		this.name=name;
		int i=0;
		while(true){
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(this,""+i++,r,b);
			arguments.add(arg);
		}
		ws_after_expr_close=r.next_empty_space();
		arguments.forEach(e->declarations.add(e.token));
		function_code=new statement(this,"c",b,"",r);
		r.toc.put("func "+name,this);
	}
	public String name(){return name;}
	@Override public void binary_to(xbin x){
//		x.def(name,this);
//		function_code.binary_to(x);
//		x.write(8);//ret // if last instr 4 set last instr 4+8
		return;
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