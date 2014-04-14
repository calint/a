package a.cap.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;

public class compiler{
	public static void main(final String[]args)throws Throwable{new compiler(args);}
	public compiler(final String[]args)throws Throwable{
		final InputStream main=getClass().getResourceAsStream("main.cap");
		final Reader mainreader=new InputStreamReader(main);
		final writer_c ccw=new writer_c();
		final Writer con=new OutputStreamWriter(System.out);
		ccw.con=new PrintWriter(con);
		
		ccw.namespace_push("cap");
		ccw.enter_path("main.cpp");
		b.b.cp(mainreader,ccw,null);
		ccw.exit_path();
		ccw.namespace_pop();
		ccw.flush();
		
		for(type_class c:ccw.classes){
			System.out.println("generate c: "+c);
			csrc_gen_class(c,ccw.con);
		}
		ccw.con.flush();
	}
	private void csrc_gen_class(type_class c,PrintWriter p)throws Throwable{
		final String nm=c.name;
//		struct file;typedef struct file file;size file_count;size file_sizeof;
		p.println("struct "+nm+";typedef struct "+nm+" "+nm+";");
//		file*file_new(void*,const size);//gives
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
		
//		void file_info(const file*,FILE*);
//		void file_to(const file*,FILE*);
//		void file_foreach_char_write(const file*,void(^)(char*));
//		void file_foreach_char(const file*,void(^)(char));
//		void file_copy(const file*,const char*,const size);
//		const size file_size_in_bytes(const file*);
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
					if(ch==';'&&token.length()==0)
						throw new Error("line "+lineno+": "+charno+": stray ';'");						
					if(is_white_space(ch)){
						if(token.length()==0)// white spaces before statement
							break;
						final String nm=token.toString();// new token
						token.setLength(0);
						if("class".equals(nm)){// [class] ...
							state_push(state_in_class_identifier);
							break;
						}
						throw new Error("line "+lineno+": "+charno+": expected 'class' declaration. found '"+nm+"'");
					}
					token.append(ch);
					break;
				case state_in_class_identifier:{
					if(token.length()==0&&is_white_space(ch))
						break;
					if(Character.isJavaIdentifierPart(ch)){
						token.append(ch);
						break;						
					}
					final String nm=token.toString();					
					token.setLength(0);

					if(!is_valid_class_identifier(nm))
						throw new Error("line "+lineno+": class identifier invalid '"+nm+"'");
					// entering class code block, new namespace
					System.err.println("class "+nm);
					classes.add(new type_class(nm));
					System.err.println("classes: "+classes);
					namespace_push(nm);
					state_push(state_after_class_identifier);
					}
				case state_after_class_identifier:// skip ws, look for {
					if(is_white_space(ch))
						break;
					if(is_char_block_open(ch)){
						state_push(state_in_class_block);
						break;
					}
					throw new Error("line "+lineno+":"+charno+" expected '{' after class name but found '"+ch+"'");
				case state_in_class_block:{// leads to: (constructor|function|field)
					if(token.length()==0&&is_white_space(ch))
						break;
					if(ch=='('){// ctor|func
						final String nm=token.toString();
						if(nm.equals(namespc.name)){
							System.out.println("found constructor "+nm);
						}else{
							System.out.println("found function "+nm);							
						}
						token.setLength(0);// ignore class block content
						namespace_push(nm);
						state_push(state_in_class_block_function_arguments);
						break;
					}
					if(is_white_space(ch)){
						final String nm=token.toString();
						if(nm.equals(namespc.name)){
							System.out.println("found constructor "+nm);
						}else{
							System.out.println("found function "+nm);							
						}
						token.setLength(0);// ignore class block content
						namespace_push(nm);
						state_push(state_find_class_block_function_arguments);
						break;						
					}
					if(is_char_block_close(ch)){
//						token.append(ch);
						token.setLength(0);// ignore class block content
						state_pop_until_state(state_in_global_statement);
						namespace_pop();
						break;
					}
					token.append(ch);
					break;
				}
				case state_find_class_block_function_arguments:{// i.e. class file{func•  •(){}}
					if(is_white_space(ch))
						break;
					if(ch=='(')
						state_push(state_in_class_block_function_arguments);
					break;
				}
				case state_in_class_block_function_arguments:{// class file{func(•size s•){}}
					if(ch==')'){
						state_push(state_find_class_block_function_block);
						break;
					}
					break;
				}
				case state_find_class_block_function_block:{// class file{func(size s) •{}•
					if(is_white_space(ch)&&token.length()==0)
						continue;
					if(ch=='{'){
						state_push(state_in_class_block_function_block);
						break;
					}
					token.append(ch);
					break;
				}
				case state_in_class_block_function_block:{// class file{func(size s){•int a=2;a+=2;•}
					if(is_white_space(ch)&&token.length()==0)
						continue;
					if(ch=='}'){
						state_pop_until_state(state_in_class_block);// find block
						break;
					}
					token.append(ch);
					break;
				}
				default:throw new Error("unknown state "+state);
				}
				charno++;
				if(ch=='\n'){
					lineno++;
					charno=1;
				}
			}
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
			namespc=namespace_stack.pop();
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
			namespc=ns;
		}
		
		final LinkedList<namespace>namespace_stack=new LinkedList<>();
		namespace namespc;
		
		int state;
		public static final int state_in_global_statement=0;
		public static final int state_in_class_identifier=1;
		public static final int state_after_class_identifier=2;
		public static final int state_in_class_block=3;
		public static final int state_find_class_block_function_arguments=4;
		public static final int state_in_class_block_function_arguments=5;
		public static final int state_find_class_block_function_block=6;
		public static final int state_in_class_block_function_block=7;
			
		private LinkedList<Integer>state_stack=new LinkedList<>();
		private ArrayList<type_class>classes=new ArrayList<>();
	}
}
final class namespace{
	String name;
	int level;
	ArrayList<String>functions=new ArrayList<>();
	ArrayList<String>fields=new ArrayList<>();
	
	@Override public String toString(){return name+"{}";}
}

final class type_class{
	String name;
	public type_class(String name){this.name=name;}//autoset
	@Override public String toString(){return name;}
}