package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import b.a;
import b.xwriter;

public class call extends statement{
	private static final long serialVersionUID=1;
	final private String name,ws_after_name,ws_trailing;
	final protected ArrayList<expression>arguments=new ArrayList<>();
	public call(a pt,String nm,statement b,LinkedHashMap<String,String>annotations,String name,reader r){
		super(pt,nm,annotations,"",r,b);
		this.name=name;
		ws_after_name=r.next_empty_space();
		while(true){
			if(r.is_next_char_expression_close()) break;
			r.set_location_in_source();
			final expression arg=new expression(pt,nm,r,b);
			arguments.add(arg);
		}
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
	}
	public call(a pt,String nm,String name,reader r,statement b){
		this(pt,nm,b,new LinkedHashMap<String,String>(),name,r);
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
		final ArrayList<String>allocated_registers=new ArrayList<>();
		final ArrayList<String>aliases=new ArrayList<>();
		for(expression e:arguments){
			final expression argspec=d.arguments.get(i);
			i++;
			if(argspec.token.equals(e.token)) 
				continue;
			final String r=x.get_register_for_alias(e.token);
			if(r!=null){
				if(x.get_register_for_alias(argspec.token)==null)
					x.alias_register(argspec.token,r);
				aliases.add(argspec.token);
				continue;
			}
			// alias if alias
			
			final String rd=x.allocate_register(this);
			allocated_registers.add(rd);
			x.alias_register(argspec.token,rd);
			aliases.add(argspec.token);
			final int rdi=x.register_index_for_alias(this,argspec.token);
//			final int rdi=a.token.charAt(0)-'a';
			final int in=0x0000|(rdi&63)<<14;
			x.write(in);
			x.write(e.eval(x));
		}
//		if(has_annotation("inline")){
			d.function_code.binary_to(x);
//		}else{
//			x.linker_add_call(name);
//			x.write(apply_znxr_annotations_on_instruction(0x0010));//call
//		}
		allocated_registers.forEach(e->x.free_register(e));
		aliases.forEach(e->x.unalias_register(this,e));
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
//		final String asm="li add foo fow inc ld ldc li lp st stc tx shf ldd dec  zkp skp";
//		final boolean is=asm.indexOf(name)!=-1;
//		x.tag(is?"ac":"fc");
		x.p(name);
//		x.tage(is?"ac":"fc");
		x.p("(").p(ws_after_name);
		arguments.forEach(e->e.source_to(x));
		x.p(")").p(ws_trailing);
	}

	public static int declared_register_index_from_string(xbin bin,statement stmt,String alias){
		final int rai=bin.register_index_for_alias(stmt,alias);
		return rai;
	}

}