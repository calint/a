package a.cap;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import a.x.cli;
import b.a;
import b.osnl;
import b.path;
import b.req;
import b.xwriter;

public class $ extends a {
	public a cap;// cap source input
	public a c;// c source compiled output
	public a sts;// compiler status
	public a out;// stdout at run
	{cap.from($.class.getResourceAsStream("main.cap"),"// cap code");}
	@Override public void to(xwriter x) throws Throwable {
		x.title("cap c sandbox");
//		x.style("html","padding:0");
		x.style(cap,"box-shadow:0 0 .5em rgba(0,0,0,.5);background:#f8f8f8;width:30em;height:128em;padding:0 1em 0 .5em");
		x.style(c,"box-shadow:0 0 .5em rgba(0,0,0,.5);background:#e8e8e8;width:256em;height:128em;padding:0 1em 0 .5em");
		x.style(sts,"display:block;border:1px solid black;padding:.5em;background:yellow");
		x.style(out,"box-shadow:0 0 .5em rgba(0,0,0,.5);display:block;background:black;color:green;border:1px dotted grey;width:40em;height:128em;padding:0 1em 0 .5em");
		x.style("div.la","width:1024em");
		x.style("div.la div.c1","float:left;color:grey;text-align:right;background:#f0f0f0;float:left;text-align:right;padding:0 .5em 0 1em");
		x.style("div.la div.c2","float:left");
		x.style("div.la div.c3","float:left");
		x.style("div.la div.c4","float:left;border-left:1px dotted grey;color:grey;text-align:rightl;background:#f0f0f0;float:left;text-align:right;padding:0 .5em 0 1em");
		x.style("div.la div.c5","float:left");
		x.style("div.la div.c6","float:left");
		x.style("div.la div.c7","float:left");
		x.style("div.la div.c8","float:left");
		x.div("la");
			x.div("c1");for(int i=1;i<100;i++)x.pl(""+i);x.divEnd();
			x.div("c2").inputTextArea(cap).focus(cap).divEnd();
			x.div("c3").spc().ax(this).nl().el(sts).elend().divEnd();
			x.div("c7").el(out).p("console output").elend().divEnd();
			x.div("c6").ax(this,"cc","••").divEnd();
//			x.div("c8").p(" :: ").ax(this,"cr","etc ").divEnd();
			x.div("c4");for(int i=1;i<100;i++)x.pl(""+i);x.divEnd();
			x.div("c5").inputTextArea(c).divEnd();
			x.pl("<br style=clear:left />");
		x.divEnd();
	}
	synchronized public void x_(xwriter y,String a)throws Throwable{
		final path basedir=req.get().session().path("a/cap");
		final path csrc=basedir.get("main.c");
		cap.to(basedir.get("main.cap"));
		final Reader in=new StringReader(cap.toString());
		final Writer out=csrc.writer(false);
		out.write("// "+new Date()+"\n");
		final cap cc=new cap();
		try{
			cc.compile(in,out);
			y.xu(sts,new Date()+" ok");
			out.close();
			c.from(csrc);
			y.xu(c);
			x_cc(y,null);
		}catch(Throwable t){
			Throwable t1=t,t2;
			while(true){
				t2=t1.getCause();
				if(t2==null)break;
				t1=t2;
			}
			y.xu(sts,new Date()+"\n"+error_line(t)+"\n\n\n stacktrace:\n"+stacktrace(t));
			out.close();
			c.from(csrc);
			y.xu(c);
//			throw t;
		}
	}
	private static String stacktrace(final Throwable e){
		final StringWriter sw=new StringWriter();
		final PrintWriter out=new PrintWriter(sw);
		e.printStackTrace(out);
		out.close();
		return sw.toString();
	}
//	private static String stacktraceline(final Throwable e){
//		return stacktrace(e).replace('\n',' ').replace('\r',' ').replaceAll("\\s+"," ").replaceAll(" at "," @ ");
//	}
	public static String error_line(Throwable t){
		Throwable t1=t,t2;
		while(true){
			t2=t1.getCause();
			if(t2==null)break;
			t1=t2;
		}
		final String msg=t1.getMessage();
		final String m=msg==null?stacktrace(t1):msg;
		return m;
	}
	
	public static String cc="gcc";
	synchronized public void x_cc(xwriter x,String a)throws Throwable{
		final path basedir=req.get().session().path("a/cap");
		final path csrc=basedir.get("main.c");
		c.to(csrc);
		final xwriter y=x.xub(out,true,true);
		try{new cli("sh",new osnl(){@Override public void onnewline(final String line)throws Throwable{
			y.pl(line);
//			System.out.println(line);
		}}).p("date&&echo&&cd ").p(basedir.toString()).p("&&pwd&&echo&&ls -lA&&echo&&echo zipped word count&&cat main.cap|gzip|wc&&cat main.c|gzip|wc&&cat main|gzip|wc&&").p(cc).p(" -o main -Wfatal-errors *.c&&echo&&echo program output&&echo -- --- --- - - --- - -- - -- - - --- - - -- -  -&&./main&&echo&&echo -- - - - - -- -- - - - - ---  ---  -- - - - -----&&echo&&date").exit();}
		finally{x.xube();}
		x.xfocus(out);
	}
	
	private static final long serialVersionUID = 1L;
}
