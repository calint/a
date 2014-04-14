package a.cap.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;

import a.cap.c.clazz.slot;

public class compiler{
	public static void main(final String[]args)throws Throwable{new compiler(args);}
	public compiler(final String[]args)throws Throwable{
		final InputStream main=getClass().getResourceAsStream("main.cap");
		final Reader mainreader=new InputStreamReader(main);
		final writer_c ccw=new writer_c();
		final Writer con=new OutputStreamWriter(System.out);
		ccw.con=new PrintWriter(con);
		
//		ccw.namespace_push("cap");
		ccw.enter_path("main.cpp");
		b.b.cp(mainreader,ccw,null);
		ccw.exit_path();
//		ccw.namespace_pop();
		ccw.flush();
		
		ccw.con.println("///----------------------------");
		ccw.con.println("/// generate h file");
		ccw.con.println("///----------------------------");
		for(clazz c:ccw.classes){
			source_h(c,ccw.con);
		}
		ccw.con.println("///----------------------------");
		ccw.con.println("/// generate c file");
		ccw.con.println("///----------------------------");
		for(clazz c:ccw.classes){
			source_c(c,ccw.con);
		}
		//. generate reflection struct
		ccw.con.flush();
	}
	private void source_c(clazz c,PrintWriter p)throws Throwable{
		final String nm=c.name;
//		p.println("typedef struct "+nm+" "+nm+";");
		p.print("struct "+nm+"{");
		for(slot i:c.slots){
			if(i.isfunc)continue;
			p.println("\n\t"+i.typeandname+";");
		}
		p.println("};");

		p.println(nm+"*"+nm+"_new(){");
//		    file*o=(file*)malloc(sizeof(file));
			p.println("\t"+nm+"*o=("+nm+"*)malloc(sizeof("+nm+"));");
//		    return o;
			p.println("\treturn o;");
		p.println("}");
//		void file_free(file*);//takes
		p.println("void "+nm+"_free("+nm+"*o){");
		p.println("\tfree(o);");
		p.println("}");		
		for(slot i:c.slots){
			if(!i.isfunc)continue;// const char*file_name_get() set(const char*name);
			final int i1=i.typeandname.lastIndexOf('*');
			final int i2=i.typeandname.lastIndexOf(' ');
			// const int*funcName
			final String name;
			final String type;
			if(i1==-1&&i2==-1){
				// constructor
				p.println(c.name+"*"+c.name+"_new("+i.args+"){}");
				continue;
//				throw new Error("confusing type and name "+i.typeandname+". expected to contain at least a ' ' or a '*'");
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
	private void source_h(clazz c,PrintWriter p)throws Throwable{
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
				p.print(c.name+"*"+c.name+"_new("+i.args+");");
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
			p.print(c.name+"_"+funcname+"("+c.name+"*o");
			if(i.args.length()>0)p.print(",");
			p.println(i.args+");");
		}
	}
	static class writer_c extends Writer{
		PrintWriter con;
		String path;
		int lineno=1,charno=1;
		void enter_path(String p){namespace_push(p);}
		private StringBuilder token=new StringBuilder(32);
		@Override public void write(char[]cbuf,final int off,final int len)throws IOException{
			int o=off,l=len;
			while(true){
				if(l==0)break;
				final char ch=cbuf[o];o++;l--;
//				con.write(ch);
				switch(state){
				case state_in_global_statement:
					if(ch==';'&&token.length()==0)throw new Error("line "+lineno+": "+charno+": stray ';'");						
					if(is_white_space(ch)){
						if(token.length()==0)break;// skip beginning white spaces
						final String tkn=token.toString();// new token
						token.setLength(0);
						if("class".equals(tkn)){
							state_push(state_in_class_ident);
							break;
						}
						throw new Error("line "+lineno+": "+charno+": expected keyword 'class' but found '"+tkn+"'");
					}
					token.append(ch);
					break;
				case state_in_class_ident:{
					if(token.length()==0&&is_white_space(ch))break;// trims leading white space
					if(ch!='{'){// look for opening class block
						token.append(ch);
						break;						
					}
					final String clsnm=clean_whitespaces(token.toString());					
					token.setLength(0);

					if(!is_valid_class_identifier(clsnm))throw new Error("line "+lineno+": invalid class name '"+clsnm+"'");
					classes.push(new clazz(clsnm));
					namespace_push(clsnm);
					state_push(state_in_class_block);
					break;
					}
//				case state_after_class_name:// skip ws, look for {
//					if(is_white_space(ch))
//						break;
//					if(is_char_block_open(ch)){
//						state_push(state_in_class_block);
//						break;
//					}
//					throw new Error("line "+lineno+":"+charno+" expected '{' after class name but found '"+ch+"'");
				case state_in_class_block:{// find type
					if(token.length()==0&&is_white_space(ch))break;// trim leading white space
					if(ch=='('){// found type+name function i.e. const 'int foo('
						final String ident=clean_whitespaces(token.toString());
						token.setLength(0);// ignore class block content
						namespace_push(ident);
						classes.peek().slots.push(new clazz.slot(ident,true));
						System.out.println(classes);
						System.err.println("found function '"+ident+"'");
						state_push(state_class_block_in_function_arguments);
						break;
					}
					if(ch==';'){// found type+name field i.e. int a;
						final String ident=clean_whitespaces(token.toString());
						token.setLength(0);// ignore class block content
						namespace_push(ident);
						classes.peek().slots.push(new clazz.slot(ident,false));
						System.out.println(classes);
						System.err.println("found field '"+ident+"'");
						state_push(state_in_class_block);
						break;
					}
					if(is_char_block_close(ch)){// close class block
						token.setLength(0);// ignore class block content
						state_pop_until_state(state_in_global_statement);
						namespace_pop();
						break;
					}
					token.append(ch);
					break;
				}
				case state_class_block_in_function_arguments:{// class file{func(•size s,int i•){}}
					if(ch==')'){
						state_push(state_class_block_find_start_of_function_block);
						final String funcargs=clean_whitespaces(token.toString());
//						System.out.println("function args: "+funcargs);
						classes.peek().slots.peek().args=funcargs;
						token.setLength(0);
						break;
					}
					token.append(ch);
					break;
				}
				case state_class_block_find_start_of_function_block:{// class file{func(size s) •{}•
					if(is_white_space(ch)&&token.length()==0)
						continue;
					if(ch=='{'){
						state_push(state_class_block_in_function_block);
//						namespace_push(namespace_stack.peek().name);
						break;
					}
					token.append(ch);
					break;
				}
				case state_class_block_in_function_block:{// class file{func(size s){•int a=2;a+=2;•}
					if(is_white_space(ch)&&token.length()==0)
						continue;
					if(ch=='}'){
						state_pop_until_state(state_in_class_block);// find block
						namespace_pop();
						break;
					}
//					token.append(ch);
					break;
				}
//				case state_class_block_in_function_name:{// class file{int •foo•(size s){int a=2;a+=2;}
//					if(Character.isJavaIdentifierPart(ch)){
//						token.append(ch);
//						break;
//					}
//					final String funcnm=token.toString();
//					System.out.println("function name "+funcnm);
//					token.setLength(0);
//					if(is_white_space(ch)){
//						state_push(state_class_block_find_function_arguments);
//						break;
//					}
//					if(ch=='('){
//						state_push(state_class_block_in_function_arguments);
//						break;
//					}
//					throw new Error("line "+lineno+": "+charno+": function name '"+funcnm+"' in class '"+classes.peek()+"' is not valid.");
//				}
				default:throw new Error("unknown state "+state);
				}
				charno++;
				if(ch=='\n'){
					lineno++;
					charno=1;
				}
			}
		}
		private static String clean_whitespaces(String s){
			return s.trim().replaceAll("\\s+"," ").replaceAll(" \\*","\\*").replaceAll("\\* ","\\*");
		}
		private void state_push(int newstate){
			state_stack.push(state);
			state=newstate;
			System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);
		}
		private void state_pop(){
			state=state_stack.pop();
			System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);
		}
		private void state_pop_until_state(final int s){
			while(true){
				state=state_stack.pop();
				if(state==s)break;
			}
		}
		public void namespace_push(String name){
			final namespace ns=new namespace();
			ns.name=name;
			namespace_push_and_activate(ns);
			System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);
		}
		private void namespace_pop(){
			namespace_stack.pop();
			System.err.println("line "+lineno+" state "+state+"  stk:"+state_stack+"   "+namespace_stack);
		}
		private boolean is_char_block_open(char ch){return ch=='{';}
		private boolean is_char_block_close(char ch){return ch=='}';}
		private boolean is_valid_class_identifier(String nm){return true;}
		private boolean is_white_space(char ch){return Character.isWhitespace(ch);}
		void exit_path(){
			namespace_pop();
		}
		@Override public void flush()throws IOException{
			con.flush();
		}
		@Override public void close()throws IOException{
			con.close();
		}
//		public void namespace_leave(){
//			final namespace ns=namespace_stack.pop();
//			current_namespace=ns;
//			con.println("namespace stack:"+namespace_stack);
//		}
		private void namespace_push_and_activate(namespace ns){
			namespace_stack.push(ns);
//			namespc=ns;
		}
		
		final LinkedList<namespace>namespace_stack=new LinkedList<>();
//		namespace namespc;
		
		int state;
		public static final int state_in_global_statement=0;
		public static final int state_in_class_ident=1;
//		public static final int state_after_class_name=2;
		public static final int state_in_class_block=3;
//		public static final int state_class_block_in_function_name=4;
		public static final int state_class_block_in_item_sign=4;
//		public static final int state_class_block_find_function_arguments=5;
		public static final int state_class_block_in_function_arguments=6;
		public static final int state_class_block_find_start_of_function_block=7;
		public static final int state_class_block_in_function_block=8;
			
		private LinkedList<Integer>state_stack=new LinkedList<>();
		private LinkedList<clazz>classes=new LinkedList<>();
	}
}
final class namespace{
	String name;
//	int level;
//	ArrayList<String>functions=new ArrayList<>();
//	ArrayList<String>fields=new ArrayList<>();
	
	@Override public String toString(){return name;}
}

final class clazz{
	String name;
	LinkedList<slot>slots=new LinkedList<>();
	
	public clazz(String name){this.name=name;}//autoset
	@Override public String toString(){return name+"{"+slots+"}";}

	final static class slot{
		String typeandname;
		String args="";
		boolean isfunc;
		public slot(String type_and_name,boolean func){typeandname=type_and_name;isfunc=func;}
		@Override public String toString(){
			return typeandname+(isfunc?("("+args+")"):"");
		}
	}
}