package a.cap;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;

import a.cap.struct.slot;
import a.cap.vm.block;
import a.cap.vm.floati;
import a.cap.vm.inti;
import a.cap.vm.str;
import a.cap.vm.type;
import a.cap.vm.var;

final public class cap{
	public String indent="   ";
//	public static void main(final String[]args)throws Throwable{
//		final cap c=new cap();
//		final InputStream is=cap.class.getResourceAsStream("main.cap");
//		final source_reader in=new source_reader(new InputStreamReader(is,b.b.strenc));
//		try{
//			c.compile(in,new OutputStreamWriter(System.out));
//		}catch(Throwable t){
//			throw new Error(source_reader.hr_location_string_from_line_and_col(in.line_number,in.character_number_in_line)+"  "+t.getMessage());
//		}
//	}
	public void compile(Reader in,Writer ccode)throws Throwable{
		final toc cc=new toc();
		cc.namespace_push("cap");
		cc.types_add(new type("stream"));
		cc.types_add(inti.t);
		cc.types_add(floati.t);
		cc.types_add(str.t);
//		cc.namespace_add_var(new var(str.t,"meta[0]"));
		final type t=cc.find_type_by_name_or_break("stream");
		cc.namespace_add_var(new var(t,"out"));
		b.b.cp(in,cc,null);
		cc.namespace_pop();		
		final PrintWriter out=new PrintWriter(ccode);
		b.b.cp(cap.class.getResourceAsStream("header"),out);
		cc.classes().forEach((c)->source_c(c,out));
//		// generate reflection struct
//		const struct struc structs[]={
//		    {"file",sizeof(file__fields)/sizeof(field),file__fields,sizeof(file__funcs)/sizeof(function),file__funcs}
//		};
		out.println("static const struct struc structs[]={");
		cc.classes().forEach((c)->{out.println("  {\""+c.name+"\",sizeof("+c.name+"__field)/sizeof(field),"+c.name+"__field,sizeof("+c.name+"__func)/sizeof(function),"+c.name+"__func},");});		
		out.println("};");
//		cc.classes().forEach((c)->{
//			out.print(c.name);
//			out.print("{");
//			for(struct.slot s:c.slots){
//				if(s.isctor)continue;
//				if(s.isfunc)continue;
//				out.print(s.type.toString());
//				out.print(" ");
//				out.print(s.name);
//				out.println("}");
//			}
//			out.println("}");
//		});
//		out.println("};");
		// include footer		
		b.b.cp(cap.class.getResourceAsStream("footer"),out);

//		final InputStream main=cap.class.getResourceAsStream("main.cap");
//		final class inc{int i;}
//		final inc i=new inc();
//		out.println("/// main.cap");
//		final osnl nl=new osnl(){@Override public void onnewline(String line)throws Throwable{
//			i.i++;
//			out.println("/// "+i.i+" : "+line);
//		}};
//		b.b.cp(main,nl);
//		nl.write(new byte[]{'\n'});
//		out.println("///");
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
//		const field file__fields[]={
//			    {"size","address",offsetof(file,address),sizeof(address)},
//			    {"size","nbytes",offsetof(file,nbytes),sizeof(nbytes)},
//			};
		p.print("static const field "+cnm+"__field[]={");
		if(!attrs.isEmpty())p.println();
		for(struct.slot i:attrs){
//		    {"size","address",offsetof(file,address),sizeof(address)},
			p.println("  {\""+i.type+"\",\""+i.name+"\",offsetof("+cnm+","+i.name+"),sizeof("+i.type+")},");
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
		p.print("static const function "+cnm+"__func[]={");
		if(!funcs.isEmpty())p.println();
		for(struct.slot i:funcs){
			p.println("  {\""+i.type+"\",\""+i.name+"\",\""+i.args_declaration_to_string()+"\","+cnm+"_"+i.name+"},");
		}
		p.println("};");
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
