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
import b.osnl;

final public class cap{
	public static void main(final String[]args)throws Throwable{
		final cap c=new cap();
		final InputStream main=cap.class.getResourceAsStream("main.cap");
		final Reader in=new InputStreamReader(main);
		c.compile(in,new OutputStreamWriter(System.out));
	}
	public void compile(Reader in,Writer ccode)throws Throwable{
		final toc cc=new toc();
		cc.namespace_enter("cap");
		b.b.cp(in,cc,null);
		cc.namespace_pop();		
		cc.out=new PrintWriter(ccode);
		b.b.cp(cap.class.getResourceAsStream("header.cap"),cc.out);
		cc.classes().forEach((c)->source_c(c,cc.out));
		//. generate reflection struct
		cc.out.println("/// main.cap done");
		b.b.cp(cap.class.getResourceAsStream("footer.cap"),cc.out);

		final InputStream main=cap.class.getResourceAsStream("main.cap");
		final class inc{int i;}
		final inc i=new inc();
		cc.out.println("/// main.cap");
		final osnl nl=new osnl(){@Override public void onnewline(String line)throws Throwable{
			i.i++;
			cc.out.println("/// "+i.i+" : "+line);
		}};
		b.b.cp(main,nl);
		nl.write(new byte[]{'\n'});
		cc.out.println("///");
		cc.out.flush();
//		System.out.println(cc.state_to_string());
	}
	private void source_c(struct c,PrintWriter p){
//		System.err.println(c.slots.size());
		final String cnm=c.name;
		p.println("typedef struct "+cnm+" "+cnm+";");
		p.print("static struct "+cnm+"{");
		final LinkedList<struct.slot>attrs=new LinkedList<>();
		final LinkedList<struct.slot>funcs=new LinkedList<>();
		for(slot i:c.slots)
			if(i.isfunc)funcs.add(i);else attrs.add(i);
		Collections.reverse(attrs);
		Collections.reverse(funcs);
		
		for(struct.slot i:attrs){
			p.print("\n\t"+i.type);
			if(!i.ispointer)p.print(" ");
			p.print(i.name+";");
		}
		p.print("\n}"+cnm+"_default={");
		for(struct.slot i:attrs){
			p.print("."+i.name+"="+i.args+",");
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
				p.println(c.name+" "+c.name+"_mk("+i.args+"){}");
				continue;
			}
			p.print("static inline "+i.type);
			if(!i.type.endsWith("*"))p.print(" ");
			p.print(c.name+"_"+i.name+"("+c.name+"*o");
			if(i.args.length()>0)p.print(",");
			//? class arguments, argument
			if(i.args.length()>0){
				if(i.args.indexOf(' ')==-1){// one word argument i.e. foo{to(stream)}
					p.print(i.args+" "+i.args.charAt(0));
				}else
					p.print(i.args);
			}
			p.println("){"+i.body+"}");
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
}
