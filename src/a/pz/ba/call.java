package a.pz.ba;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import b.a;
import b.xwriter;

public class call extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name,ws_trailing;
	final protected ArrayList<expression> arguments=new ArrayList<>();
	public call(a pt,String nm,LinkedHashMap<String,String> annotations,String name,reader r,block b){
		super(pt,nm,annotations,"",r,b);
		this.name=name;
		ws_after_name=r.next_empty_space();
		while(true){
			if(r.is_next_char_expression_close()) break;
			final expression arg=new expression(pt,nm,no_annotations,r,b);
			arguments.add(arg);
		}
		ws_trailing=r.next_empty_space();
	}
	public call(a pt,String nm,String name,reader r,block b){
		this(pt,nm,new LinkedHashMap<String,String>(),name,r,b);
	}
	protected int apply_znxr_annotations_on_instruction(int i){
		int znxr=0;
		if(has_annotation("ifp")) znxr|=3;
		if(has_annotation("ifz")) znxr|=1;
		if(has_annotation("ifn")) znxr|=2;
		if(has_annotation("nxt")) znxr|=4;
		if(has_annotation("ret")) znxr|=8;
		return znxr|i;
	}
	@Override public void binary_to(xbin x){
		final def_func d=(def_func)x.toc.get("func "+name);
		if(d==null) throw new compiler_error(this,"function not found",name);
		if(arguments.size()!=d.arguments.size())
			throw new compiler_error(this,"function "+name+" expects "+d.arguments.size()+" arguments, got "+arguments.size(),"");
		int i=0;
		for(expression e:arguments){
			final expression a=d.arguments.get(i);
			if(a.token.equals(d.arguments.get(i).token)) continue;
			i++;
			final int rdi=a.token.charAt(0)-'a';
			final int in=0x0000|(0&15)<<8|(rdi&15)<<12;
			x.write(in);
			x.write(e.eval(x));
		}
		if(has_annotation("inline")){
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

	public static int declared_register_index_from_string(xbin bin,statement stmt,String token){
		final String reg=bin.register_for_alias(token);
		if(reg!=null)
			token=reg;
//		if(reg==null)
//			throw new compiler_error(stmt,"alias not found in "+bin.register_aliases,token);
//		token=reg;
		if(token.length()!=1) throw new compiler_error(stmt,"not a register",token);
		if(!stmt.blk.is_register_declared(token))
			throw new compiler_error(stmt,"register not declared",token);
		final int rai=token.charAt(0)-'a';
		if(rai<0||rai>15) throw new compiler_error(stmt,"registers 'a' through 'p' available",token);
		return rai;
	}

}