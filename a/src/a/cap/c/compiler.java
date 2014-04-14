package a.cap.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class compiler{
	public static void main(final String[]args)throws Throwable{new compiler(args);}
	public compiler(final String[]args)throws Throwable{
		final InputStream main=getClass().getResourceAsStream("main.cap");
		final Reader mainreader=new InputStreamReader(main);
		final writer_c ccw=new writer_c();
		final Writer con=new OutputStreamWriter(System.out);
		ccw.con=con;
		ccw.enter_path("main.cpp");
		b.b.cp(mainreader,ccw,null);
		ccw.flush();
	}
	static class writer_c extends Writer{
		Writer con;
		String path;
		int lineno;
		void enter_path(String p){
			new PrintWriter(this).println("   enter path: "+p);
		}
		@Override public void write(char[]cbuf,int off,int len)throws IOException{
			con.write(cbuf,off,len);
		}
		@Override public void flush()throws IOException{
			con.flush();
		}
		@Override public void close()throws IOException{
			con.close();
		}
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
final class type_class{
	String name;
	ArrayList<String>functions=new ArrayList<>();
	ArrayList<String>fields=new ArrayList<>();
}