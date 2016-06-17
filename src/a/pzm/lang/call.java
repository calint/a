package a.pzm.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import a.pzm.lang.reader.token;
import b.xwriter;

public class call extends expression{
	private static final long serialVersionUID=1;
	final protected ArrayList<expression>arguments=new ArrayList<>();
	final private boolean dest_reg_was_null;
	public call(statement parent,annotations annot,token funcname,reader r,String dest_reg){
		super(parent,annot,funcname,r,dest_reg);
		destreg=dest_reg;
		if(dest_reg!=null){// return register first arg
			final expression e=new expression(this,dest_reg);
			arguments.add(e);
			mark_end_of_source(r);
			dest_reg_was_null=false;
		}else{
			dest_reg_was_null=true;
		}
		while(true){
			mark_end_of_source(r);
			if(r.is_next_char_expression_close())break;
			r.set_location_cue();
			final expression arg=expression.read(this,null,null,r);
			arguments.add(arg);
		}
		mark_end_of_source(r);
	}
	public call(statement parent,annotations annot,token function_name,reader r){
		this(parent,annot,function_name,r,null);
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
//		final String funcs=x.toc.keySet().stream()
//				.filter(s->s.startsWith("func "))
//				.map(s->s.subSequence("func ".length(),s.length()))
//				.collect(Collectors.toList())
//				.toString();
		final ArrayList<String>ls=new ArrayList<>();
		for(final String s:x.toc.keySet()){
			if(!s.startsWith("func "))continue;
			final String s1=s.substring("func ".length());
			ls.add(s1);
		}
		final String funcs=ls.toString();
		if(d==null)throw new compiler_error(this,"function '"+token+"' not found",funcs);
		if(destreg!=null && dest_reg_was_null){// return register first arg
			final expression e=new expression(this,destreg);
			e.source_location_start=source_location_start;
			e.source_location_end=source_location_end;
			arguments.add(0,e);
		}
		if(arguments.size()!=d.params.size())
			throw new compiler_error(this,"function "+token+" expects "+d.params.size()+" arguments, got "+arguments.size(),"");
		int i=0;
		final LinkedHashMap<String,String>aliases=new LinkedHashMap<>();
		final ArrayList<String>allocated_vars=new ArrayList<>();
		for(expression ea:arguments){
			final def_func_param df=d.params.get(i++);
//			if(ea.destreg!=null)throw new compiler_error(this,"expected destination register to be null",null);
			if(x.vspc().is_declared(ea.token.name)){
				aliases.put(df.token.name,ea.token.name);
			}else{
				if(ea.source_location_start==null)
					System.out.println("");
				String nm="_"+ea.source_lineno()+"_"+i;
//				final String reg=x.alloc_register(ea);
//				ea.destreg=reg;
				x.vspc().alloc_var(ea,nm);
				allocated_vars.add(nm);
				ea.destreg=nm;
				aliases.put(df.token.name,ea.destreg);
				ea.binary_to(x);
			}
		}
		x.push_func();
		for(Map.Entry<String,String>e:aliases.entrySet())
			x.vspc().alias(this,e.getKey(),e.getValue());
		d.function_code.binary_to(x);
		x.pop(this);
		for(String varnm:allocated_vars)
			x.vspc().free_var(this,varnm);
		if(destreg!=null && dest_reg_was_null){// return register first arg
			arguments.remove(0);
		}
	}
	@Override public void source_to(xwriter x){
		super.source_to(x);
		x.p("(");
		if(destreg==null)for(final expression e:arguments)e.source_to(x);
		else for(final expression e:arguments.subList(1,arguments.size()))e.source_to(x);
		x.p(")");
	}
	protected void ensure_arg_count(int i) {
		if(arguments!=null&&arguments.size()==i)
			return;
		throw new compiler_error(this,"expected "+i+" arguments, got "+(arguments!=null?arguments.size():0),"");
	}

//	public static int declared_register_index_from_string(xbin bin,statement stmt,String alias){
//		final int rai=bin.vspc().get_register_index(stmt,alias);
//		return rai;
//	}

}