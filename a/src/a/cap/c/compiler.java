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
	}
	static class writer_c extends Writer{
		PrintWriter con;
		String path;
		int lineno=1,charno=1;
		void enter_path(String p){
			state_stack.push(state);
			namespace_push(p);
		}
		private StringBuilder current_token=new StringBuilder(32);
		@Override public void write(char[]cbuf,final int off,final int len)throws IOException{
			int o=off,l=len;
			while(true){
				if(l==0)break;
				final char ch=cbuf[o];o++;l--;
				con.write(ch);
				switch(state){
				case state_find_statement:
					if(is_white_space(ch))
						break;
					state_push(state_new_statement);
				case state_new_statement:
					if(is_white_space(ch)){
						ontoken(current_token.toString());
						current_token.setLength(0);
						break;
					}
					current_token.append(ch);
					break;
				case state_new_class_identifier:
					if(Character.isJavaIdentifierPart(ch)){
						current_token.append(ch);
						break;						
					}
					if(!is_char_block_open(ch))
						throw new Error("line "+lineno+": expected {");					
					ontoken(current_token.toString());
					current_token.setLength(0);
					break;
				case state_new_class_block:
					if(is_char_block_close(ch)){
						System.err.println(state_stack);
						System.err.println(namespace_stack);
						state_pop();// state_new_class_block
						state_pop();// state_new_class_identifier
						state_pop();// state_new_class
						state_pop();// state_find_statement
						namespace_pop();
						System.err.println(state_stack);
						System.err.println(namespace_stack);
						break;
					}
					break;
				default:throw new Error("unknown state "+state);
				}
				charno++;
				if(ch=='\n'){
					lineno++;
					charno=1;
				}
			}
		}
		private void ontoken(String nm){
//			con.println("token: "+nm);
			switch(state){
			case state_new_statement:
				if("class".equals(nm)){// [class] ...
					state_push(state_new_class_identifier);
					break;
				}
				throw new Error("expected [class] declaration.");
			case state_new_class_identifier:
				if(!is_valid_class_identifier(nm))
					throw new Error("line "+lineno+": class identifier invalid ["+nm+"]");
				// entering class code block, new namespace
				System.err.println("found class "+nm);
				namespace_push(nm);
				state_push(state_new_class_block);
				break;
			case state_new_class_block:
				break;
			default:throw new Error("unknown state "+state);
			}
		}
		private void state_push(int newstate){
			state_stack.push(newstate);
			state=newstate;
		}
		private void state_pop(){state=state_stack.pop();}
		private void namespace_pop(){current_namespace=namespace_stack.pop();}
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
		public void namespace_push(String name){
			final namespace ns=new namespace();
			ns.name=name;
			namespace_push_and_activate(ns);
			con.println("namespace stack:"+namespace_stack);
		}
//		public void namespace_leave(){
//			final namespace ns=namespace_stack.pop();
//			current_namespace=ns;
//			con.println("namespace stack:"+namespace_stack);
//		}
		private void namespace_push_and_activate(namespace ns){
			namespace_stack.push(ns);
			current_namespace=ns;
		}
		
		final LinkedList<namespace>namespace_stack=new LinkedList<>();
		namespace current_namespace;
		//  
		//  /*s0*/class/*s1*/file/*s2*/{}/*s3*/;/*s0*/
		
		int state;
		public static final int state_find_statement=0;
		public static final int state_new_statement=1;
		public static final int state_new_class=2;
		public static final int state_new_class_identifier=3;
		public static final int state_new_class_block=4;
			
		private LinkedList<Integer>state_stack=new LinkedList<>();
	}
}
final class namespace{
	String name;
	int level;
	ArrayList<String>functions=new ArrayList<>();
	ArrayList<String>fields=new ArrayList<>();
	
	@Override public String toString(){return name+"{}";}
}