package a.cap.c;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import a.cap.c.toc.struct;
import a.cap.c.toc.struct.slot;

final public class cap{
	public static void main(final String[]args)throws Throwable{new cap(args);}
	public cap(final String[]args)throws Throwable{
		final InputStream main=getClass().getResourceAsStream("main.cap");
		final Reader in=new InputStreamReader(main);
		final toc cc=new toc();
		cc.out=new PrintWriter(new OutputStreamWriter(System.out));
		
		cc.namespace_enter("cap");
		b.b.cp(in,cc,null);
		cc.namespace_pop();
		
		cc.out.println("///----------------------------");
		cc.out.println("/// generate h file");
		cc.out.println("///----------------------------");
		cc.classes().forEach((c)->source_h(c,cc.out));
		cc.out.println("///----------------------------");
		cc.out.println("/// generate c file");
		cc.out.println("///----------------------------");
		cc.classes().forEach((c)->source_c(c,cc.out));
		//. generate reflection struct
		cc.out.flush();
	}
	private void source_c(struct c,PrintWriter p){
		final String cnm=c.name;
//		p.println("typedef struct "+nm+" "+nm+";");
		p.print("struct "+cnm+"{");
		for(slot i:c.slots){
			if(i.isfunc)continue;
			p.println("\n\t"+i.typeandname+";");
		}
		p.println("};");
		p.println(cnm+"*"+cnm+"_new(){return("+cnm+"*)malloc(sizeof("+cnm+"));}");
		p.println("void "+cnm+"_free("+cnm+"*o){free(o);}");		
		for(slot i:c.slots){
			if(!i.isfunc)continue;// const char*file_name_get() set(const char*name);
			final int i1=i.typeandname.lastIndexOf('*');
			final int i2=i.typeandname.lastIndexOf(' ');
			// const int*func()    const int func()
			final String name;
			final String type;
			if(i1==-1&&i2==-1){// constructor
				if(!i.typeandname.equals(c.name))throw new Error("expected constructor but found '"+i.typeandname+"'");
				p.println(c.name+"*"+c.name+"_new("+i.args+"){}");
				continue;
			}
			if(i1>i2){
				name=i.typeandname.substring(i1+1);
				type=i.typeandname.substring(0,i1+1);
			}else{
				name=i.typeandname.substring(i2+1);
				type=i.typeandname.substring(0,i2);
			}
			p.print(type);
			if(!type.endsWith("*"))p.print(" ");
			p.print(c.name+"_"+name+"("+c.name+"*o");
			if(i.args.length()>0)p.print(",");
			p.println(i.args+"){}");
		}
	}
	private void source_h(struct c,PrintWriter p){
		final String name=c.name;
		p.println("\ntypedef struct "+name+" "+name+";");
		p.println(name+"*"+name+"_new();");
		p.println("void "+name+"_free("+name+"*);");
		for(slot i:c.slots){
			if(!i.isfunc)continue;// const char*file_name_get() set(const char*name);
			final int i1=i.typeandname.lastIndexOf('*');
			final int i2=i.typeandname.lastIndexOf(' ');
			// const int*funcName
			final String funcname;
			final String returntype;
			if(i1==-1&&i2==-1){
				// constructor
				p.println(c.name+"*"+c.name+"_new("+i.args+");");
				continue;
			}
			if(i1>i2){
				funcname=i.typeandname.substring(i1+1);
				returntype=i.typeandname.substring(0,i1+1);
			}else{
				funcname=i.typeandname.substring(i2+1);
				returntype=i.typeandname.substring(0,i2);
			}
			p.print(returntype);
			if(!returntype.endsWith("*"))p.print(" ");
			p.print(c.name+"_"+funcname+"("+c.name+"*");
			if(i.args.length()>0)p.print(",");
			p.println(i.args+");");
		}
	}
}
