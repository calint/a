package a.pz.ba;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class call extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name,ws_trailing;
	final protected ArrayList<expression> arguments=new ArrayList<>();
//	final Map<String,String> annotations_ws;
	public call(a pt,String nm,LinkedHashMap<String,String> annotations,String name,reader r){
		super(pt,nm,annotations,"",r);
		this.name=name;
//		this.annotations_ws=annotations_ws;
		ws_after_name=r.next_empty_space();
		while(true){
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(pt,nm,r);
			arguments.add(arg);
		}
		ws_trailing=r.next_empty_space();
	}
	public call(a pt,String nm,String name,reader r){
		this(pt,nm,new LinkedHashMap<String,String>(),name,r);
	}
	protected int apply_znxr_annotations_on_instruction(int i){
		int znxr=0;
		if(has_annotation("@ifp")) znxr|=3;
		if(has_annotation("@ifz")) znxr|=1;
		if(has_annotation("@ifn")) znxr|=2;
		if(has_annotation("@nxt")) znxr|=4;
		if(has_annotation("@ret")) znxr|=8;
		return znxr|i;
	}
	@Override public void binary_to(xbin x){
		final def_func d=(def_func)x.toc.get("func "+name);
		if(d==null) throw new Error("function not found: "+name);
		if(arguments.size()!=d.arguments.size()) throw new Error("number of arguments do not match: "+name);
		int i=0;
		for(expression e:arguments){
			final expression a=d.arguments.get(i);
			if(a.src.equals(d.arguments.get(i).src)) continue;
			i++;
			final int rdi=a.src.charAt(0)-'a';
			final int in=0x0000|(0&15)<<8|(rdi&15)<<12;
			x.write(in);
			x.write(e.eval(x));
		}
		if(has_annotation("@inline")){
			d.function_code.binary_to(x);
		}else{
			x.link_call(name);
			x.write(apply_znxr_annotations_on_instruction(0x0010));//call
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		final String asm="li add foo fow inc ld ldc li lp st stc tx shf   zkp skp";
		final boolean is=asm.indexOf(name)!=-1;
		x.tag(is?"ac":"fc");
		x.p(name);
		x.tage(is?"ac":"fc");
		x.p("(").p(ws_after_name);
		arguments.forEach(e->e.source_to(x));
		x.p(")").p(ws_trailing);
	}
}