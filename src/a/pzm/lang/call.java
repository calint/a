package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import b.xwriter;

public class call extends statement{
	private static final long serialVersionUID=1;
	final private String ws_left,ws_after_name,ws_trailing;
	final protected ArrayList<expression>arguments=new ArrayList<>();
	public call(statement parent,LinkedHashMap<String,String>annot,String function_name,reader r){
		super(parent,annot);
		ws_left=r.next_empty_space();
		mark_start_of_source(r);
		token=function_name;
		mark_end_of_source(r);
		ws_after_name=r.next_empty_space();
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_expression_close())break;
			r.set_location_in_source();
			final expression arg=new expression(this,null,r,null,null);
			arguments.add(arg);
		}
		mark_end_of_source(r);
		ws_trailing=r.next_empty_space();
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
		final def_func d=(def_func)x.toc.get("func "+token);
		final String funcs=x.toc.keySet().stream()
				.filter(s->s.startsWith("func "))
				.map(s->s.subSequence("func ".length(),s.length()))
				.collect(Collectors.toList())
				.toString();
		if(d==null)throw new compiler_error(this,"function '"+token+"' not found",funcs);
		if(arguments.size()!=d.params.size())throw new compiler_error(this,"function "+token+" expects "+d.params.size()+" arguments, got "+arguments.size(),"");
		x.push_func();
		int i=0;
		for(expression e:arguments){
			final def_func_param df=d.params.get(i++);
			if(x.vspc().parent().is_declared(e.token)){
				x.vspc().alias(e,df.token,e.token);
				if(e.is_pointer_dereference){
					x.vspc().alloc_var(e,df.token);
					e.destreg=df.token;
					e.binary_to(x);
					continue;
				}
				continue;
			}
			x.vspc().alloc_var(e,df.token);
			e.destreg=df.token;
			e.binary_to(x);
		}
		d.function_code.binary_to(x);
		x.pop(this);
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
//		final String asm="li add foo fow inc ld ldc li lp st stc tx shf ldd dec  zkp skp";
//		final boolean is=asm.indexOf(name)!=-1;
//		x.tag(is?"ac":"fc");
		x.p(ws_left).p(token).p(ws_after_name);
//		x.tage(is?"ac":"fc");
		x.p("(");
		arguments.forEach(e->e.source_to(x));
		x.p(")").p(ws_trailing);
	}

//	public static int declared_register_index_from_string(xbin bin,statement stmt,String alias){
//		final int rai=bin.vspc().get_register_index(stmt,alias);
//		return rai;
//	}

}