package a.cap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;

import a.cap.toc.struct;
import a.cap.toc.struct.slot;
import a.cap.vm.block;
import a.cap.vm.floati;
import a.cap.vm.inti;
import a.cap.vm.str;
import a.cap.vm.type;
import a.cap.vm.var;
import b.osnl;

final public class cap{
	public String indent="   ";
	public static void main(final String[]args)throws Throwable{
		final cap c=new cap();
		final InputStream main=cap.class.getResourceAsStream("main.cap");
		final Reader in=new InputStreamReader(main);
		c.compile(in,new OutputStreamWriter(System.out));
	}
	public void compile(Reader in,Writer ccode)throws Throwable{
		final toc cc=new toc();
		cc.namespace_push("cap");
		cc.types_add(new type("stream"));
		cc.types_add(inti.t);
		cc.types_add(floati.t);
		cc.types_add(str.t);
		final type t=cc.find_type_by_name_or_break("stream");
		cc.namespace_add_var(new var(t,"out"));
		b.b.cp(in,cc,null);
		cc.namespace_pop();		
		final PrintWriter out=new PrintWriter(ccode);
		b.b.cp(cap.class.getResourceAsStream("header.c"),out);
		cc.classes().forEach((c)->source_c(c,out));
		//. generate reflection struct
//		out.println("/// main.cap done");
		b.b.cp(cap.class.getResourceAsStream("footer.c"),out);

		final InputStream main=cap.class.getResourceAsStream("main.cap");
		final class inc{int i;}
		final inc i=new inc();
		out.println("/// main.cap");
		final osnl nl=new osnl(){@Override public void onnewline(String line)throws Throwable{
			i.i++;
			out.println("/// "+i.i+" : "+line);
		}};
		b.b.cp(main,nl);
		nl.write(new byte[]{'\n'});
		out.println("///");
		out.flush();
//		System.out.println(cc.state_to_string());
	}
	private void source_c(struct c,PrintWriter p){
//		System.err.println(c.slots.size());
		final String cnm=c.name;
		final LinkedList<struct.slot>attrs=new LinkedList<>();
		final LinkedList<struct.slot>funcs=new LinkedList<>();
		for(slot i:c.slots)
			if(i.isfunc)funcs.add(i);else attrs.add(i);
		Collections.reverse(attrs);
		Collections.reverse(funcs);
		
		p.print("typedef struct "+cnm+" "+cnm+";");
		p.print("static struct "+cnm+"{");
		for(struct.slot i:attrs){
			p.print("\n"+indent()+i.type);
			if(!i.ispointer)p.print(" ");
			p.print(i.name+";");
		}
		if(!attrs.isEmpty())p.print("\n");
		p.print("}"+cnm+"_default={");
		for(struct.slot i:attrs){
			p.print(i.struct_member_default_value+",");
//			p.print(",");
		}
		p.println("};");
//		p.println("typedef struct "+cnm+" "+cnm+";");
		p.println("static inline "+cnm+" "+cnm+"_mk(){return "+cnm+"_default;}///keep stack pointer");
		// getter/setter
		for(slot i:attrs){
			p.print("static inline "+i.type);
			if(!i.ispointer)p.print(" ");
			p.print(cnm+"_"+i.name+"(const "+cnm+"*o");
			p.println("){return o->"+i.name+";}");
			p.print("static inline void "+cnm+"_"+i.name+"_("+cnm+"*o,"+i.type);
			if(!i.ispointer)p.print(" ");
			p.println("v){o->"+i.name+"=v;}");
		}
		for(slot i:funcs){
			if(i.isctor){
				if(!i.tn.equals(c.name))throw new Error("expected constructor but found '"+i.tn+"'");
				p.println(c.name+" "+c.name+"_mk("+i.args_to_string()+"){}");
				continue;
			}
			p.print("static inline "+i.type);
			if(!i.type.endsWith("*"))p.print(" ");
			p.print(c.name+"_"+i.name+"("+c.name+"*o");
			final String args=i.args_to_string();
			if(args.length()>0){
				p.print(",");
				p.print(args);
			}
//			//? class arguments, argument
//			if(args.length()>0){
//				if(args.indexOf(' ')==-1){// one word argument i.e. foo{to(stream)}
//					p.print(args+" "+args.charAt(0));
//				}else
//					p.print(args);
//			}
			p.print(")");
			final boolean isblk=i.stm instanceof block;
			if(!isblk)p.print("{");
			p.print(i.stm);
			if(!isblk)p.print("}");
			p.println();
		}
	}
//	private void source_h(struct c,PrintWriter p){
//		final String name=c.name;
//		p.println("\ntypedef struct "+name+" "+name+";");
//		p.println(name+"*"+name+"_new();");
//		p.println("void "+name+"_free("+name+"*);");
//		for(slot i:c.slots){
//			if(!i.isfunc)continue;// const char*file_name_get() set(const char*name);
//			if(i.isctor){
//				if(!i.tn.equals(c.name))throw new Error("expected constructor for '"+c.name+"' but found '"+i.tn+"'");
//				p.println(c.name+"*"+c.name+"_new("+i.args+");");
//				continue;
//			}
//			p.print(i.type);
//			if(!i.type.endsWith("*"))p.print(" ");
//			p.print(c.name+"_"+i.name+"("+c.name+"*");
//			if(i.args.length()>0)p.print(",");
//			p.println(i.args+");");
//		}
//	}
	private String indent(){return indent;}
}
