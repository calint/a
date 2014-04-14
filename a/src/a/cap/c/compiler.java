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
		ccw.namespace_enter("cap");
		ccw.enter_path("main.cpp");
		b.b.cp(mainreader,ccw,null);
		ccw.exit_path();
		ccw.namespace_leave();
		ccw.flush();
	}
	static class writer_c extends Writer{
		PrintWriter con;
		String path;
		int lineno;
		void enter_path(String p){
			namespace_enter(p);
		}
		@Override public void write(char[]cbuf,int off,int len)throws IOException{
			con.write(cbuf,off,len);
		}
		void exit_path(){
			namespace_leave();
		}
		@Override public void flush()throws IOException{
			con.flush();
		}
		@Override public void close()throws IOException{
			con.close();
		}
		public void namespace_enter(String name){
			final namespace ns=new namespace();
			ns.name=name;
			namespace_push_and_activate(ns);
			con.println("namespace stack:"+namespace_stack);
		}
		public void namespace_leave(){
			final namespace ns=namespace_stack.pop();
			current_namespace=ns;
			con.println("namespace stack:"+namespace_stack);
		}
		private void namespace_push_and_activate(namespace ns){
			namespace_stack.push(ns);
			current_namespace=ns;
		}
		
		final LinkedList<namespace>namespace_stack=new LinkedList<>();
		namespace current_namespace;
	}
	
	
//  
//  /*s0*/class/*s1*/file/*s2*/{}/*s3*/;/*s0*/
	
	int state;
	public final int state_new_statement=0;
	public final int state_new_class=1;
	public final int state_new_class_identifier=2;
	public final int state_new_class_block=3;
	
	
	private ArrayList<Integer>state_stack=new ArrayList<>();
}
final class namespace{
	String name;
	int level;
	ArrayList<String>functions=new ArrayList<>();
	ArrayList<String>fields=new ArrayList<>();
	
	@Override public String toString(){return name+"{}";}
}