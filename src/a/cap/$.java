package a.cap;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import a.x.cli;
import b.a;
import b.b;
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
		x.style(c,"box-shadow:0 0 .5em rgba(0,0,0,.5);background:#e8e8e8;width:30em;height:128em;padding:0 1em 0 .5em");
		x.style(sts,"border:1px dotted red;padding:.5em;background:yellow");
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
			x.div("c3").spc().ax(this).p(" to c ").el(sts).elend().divEnd();
			x.div("c4");for(int i=1;i<100;i++)x.pl(""+i);x.divEnd();
			x.div("c5").inputTextArea(c).divEnd();
			x.div("c6").p(" :: ").ax(this,"cc","run ").divEnd();
			x.div("c7").el(out).p("console output").elend().divEnd();
			x.div("c8").p(" :: ").ax(this,"cr","etc ").divEnd();
			x.pl("<br style=clear:left />");
		x.divEnd();
	}
	synchronized public void x_(xwriter y,String a)throws Throwable{
		final path basedir=req.get().session().path("a/cap");
		final path csrc=basedir.get("main.c");
		final Reader in=new StringReader(cap.toString());
		final Writer out=csrc.writer(false);
		final cap cc=new cap();
		cc.compile(in,out);
		out.close();
		c.from(csrc);
		y.xu(sts,"ok");
		y.xu(c);
	}
	public static String cc="gcc";
	synchronized public void x_cc(xwriter x,String a)throws Throwable{
		final path basedir=req.get().session().path("a/cap");
		final path csrc=basedir.get("main.c");
		c.to(csrc);
		final xwriter y=x.xub(out,true,true);
		try{final cli c=new cli("sh",new osnl(){@Override public void onnewline(final String line)throws Throwable{
			y.pl(line);
//			System.out.println(line);
		}}).p("date&&echo&&cd ").p(basedir.toString()).p("&&pwd&&echo&&ls -lA&&echo&&echo zipped word count&&cat "+csrc.name()+"|gzip|wc&&").p(cc).p(" -o main -Wfatal-errors *.c&&echo&&echo program output&&echo -- --- --- - - --- - -- - -- - - --- - - -- -  -&&./main&&echo&&echo -- - - - - -- -- - - - - ---  ---  -- - - - -----&&echo&&date").exit();}
		finally{x.xube();}
		x.xfocus(out);
	}
	
	private static final long serialVersionUID = 1L;
}
