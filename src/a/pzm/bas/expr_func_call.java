package a.pzm.bas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import a.pzm.bas.assembly.call;
import b.xwriter;

final public class expr_func_call extends expr{
		public String function_name;
		public List<stmt> args=new ArrayList<>();
		public expr_func_call(final program p,final String function) throws IOException{
			super(p,function);
			function_name=function;
			while(true){
				if(p.is_next_char_paranthesis_right())
					break;
				final stmt a=p.next_statement(false);
				args.add(a);
				if(p.is_next_char_comma())
					continue;
			}
			final xwriter x=new xwriter();
			x.p(function_name).p("(");
			final Iterator<stmt> i=args.iterator();
			while(true){
				if(!i.hasNext())
					break;
				x.p(i.next().toString());
				if(i.hasNext())
					x.p(",");
			}
			x.p(")");
			txt=x.toString();
		}
		@Override public void compile(program p){
			final def_func f=p.functions.get(function_name);
			if(f==null)
				throw new compiler_error(this,"function not found",function_name);
			int ii=0;
			int instructions_count=0;
			for(stmt e:args){
				final def_func_arg fa=f.args.get(ii++);
				// alloc to_register
				e.to_register=fa.name();
				if(!fa.name().equals(e.txt)){
//					p.allocate_register(this,e.to_register);
					final xwriter x=new xwriter();
					x.p("var").spc().p(e.to_register).p("=");
					if(e instanceof constexpr){
						x.p(Integer.toString(((constexpr)e).eval(p),16));
					}else{
						x.p(e.txt());
					}
					final String insert_source=x.toString();
					final program pp=new program(insert_source);
					pp.compile(pp);
					e.bin=pp.bin;
					instructions_count+=pp.bin.length;
				}else{
//				final String type_in_register=p.type_for_register(this,e.toString());
//				if(!fa.type.equals(type_in_register))
//					throw new compiler_error(this," argument "+ii+"  expected type '"+fa.type+"' but var '"+fa.name+"' is of type '"+type_in_register+"'\n  "+f);
					e.compile(p);
					if(e.bin!=null){
						instructions_count+=e.bin.length;
					}
				}
			}
			instructions_count++;
			// + release allocated registers
			bin=new int[instructions_count];
		}
		@Override public void link(program p){
			final def_func f=p.functions.get(function_name);
			final ArrayList<Integer>b=new ArrayList<>();
			int ii=0;
			for(stmt e:args){
				final def_func_arg fa=f.args.get(ii++);
				if(!fa.name().equals(e.txt)){
					final xwriter x=new xwriter();
					x.p("var").spc().p(e.to_register).p("=");
					if(e instanceof constexpr){
						x.p(Integer.toString(((constexpr)e).eval(p),16));
					}else{
						x.p(e.txt());
					}
					final String insert_source=x.toString();
					final program pp=new program(insert_source);
					pp.build();
					e.bin=pp.bin;
				}else{
					e.link(p);
				}
				if(e.bin!=null){
					for(int i:e.bin)
						b.add(i);
				}
			}
//			if(f==null)
//				throw new compiler_error(this,"function not found",function_name);
			final int a=f.location_in_binary;
			b.add(call.op);
			bin=new int[b.size()];int ix=0;for(int i:b)bin[ix++]=i;
			bin[bin.length-1]|=(a<<6);
		}
		private static final long serialVersionUID=1;
	}