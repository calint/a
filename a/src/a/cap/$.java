package a.cap;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import a.x.cli;
import b.a;
import b.b.conf;
import b.osnl;
import b.path;
import b.req;
import b.xwriter;

public class $ extends a {
	public a cap;// cap source input
	public a c;// c source compiled output
	public a sts;// compiler status
	public a out;// stdout at run
	public a ln;// linenumbers marker
	public a bt;{bt.set("c");}// build type  pczero bootable='b' or console='c'
	public a c6;// design holder
	{cap.from($.class.getResourceAsStream("main.cap"),"// cap code");}
	@Override public void to(xwriter x) throws Throwable {
		x.title("cap c sandbox");
//		x.style("html","padding:0");
		x.style(cap,"white-space:pre;word-wrap:normal;overflow-x:scroll;box-shadow:0 0 .5em rgba(0,0,0,.5);background:#f8f8f8;width:30em;height:128em;padding:0 1em 0 .5em");
		x.style(c,"display:block;box-shadow:0 0 .5em rgba(0,0,0,.5);background:#e8e8e8;width:256em;height:128em;padding:0 1em 0 .5em");
		x.style(sts,"display:block;border:1px solid black;padding:.5em;background:yellow");
		x.style(out,"word-wrap:break-word;box-shadow:0 0 .5em rgba(0,0,0,.5);display:block;background:black;color:green;border:1px dotted grey;width:40em;height:128em;padding:0 1em 0 .5em");
		x.style("div.la","width:1024em");
		x.style(ln,"float:left;color:grey;text-align:right;background:#f0f0f0;float:left;text-align:right;padding:0 .5em 0 1em");
		x.style("div.la div.c2","float:left");
		x.style("div.la div.c3","float:left");
		x.style("div.la div.c4","float:left;border-left:1px dotted grey;color:grey;text-align:rightl;background:#f0f0f0;float:left;text-align:right;padding:0 .5em 0 1em");
		x.style("div.la div.c5","float:left");
//		x.style("div.la div.c6","float:left");
		x.style(c6,"display:block;float:left");
		x.style("div.la div.c7","float:left");
		x.style("div.la div.c8","float:left");
		x.div("la");
			x.el(ln);for(int i=1;i<100;i++)x.pl(""+i);x.elend();
			x.div("c2").inputTextArea(cap).focus(cap).divEnd();
			x.div("c3").spc().ax(this).nl().el(sts).elend().divEnd();
			x.div("c7").el(out).p("console output").elend().divEnd();
			x.el(c6);upd_c6(x);x.elend();
//			x.div("c8").p(" :: ").ax(this,"cr","etc ").divEnd();
			x.div("c4");for(int i=1;i<100;i++)x.pl(""+i);x.divEnd();
			x.div("c5").el(c).elend();
			x.pl("<br style=clear:left />");
		x.divEnd();
	}
	private void upd_c6(xwriter x){
	    x.ax(this,"cc","••").nl();
	    final String mode=bt.toString();
		if(mode.equals("c"))
			x.p("console");
		else x.ax(this,"buildmode","c","console");
		x.nl();
		if(mode.equals("b"))
			x.p("bootable");
		else x.ax(this,"buildmode","b","bootable");		
	}
	synchronized public void x_buildmode(xwriter x,String a)throws Throwable{
		bt.set(a);
		xwriter y=x.xub(c6,true,false);upd_c6(y);x.xube();
		x_(x,a);
	}
	private static void upd(xwriter x,a e,boolean html_esc)throws Throwable{
		final xwriter y=x.xub(e,true,html_esc);
		y.xu(e);
		x.xube();
	}
	synchronized public void x_(xwriter y,String a)throws Throwable{
		final path basedir=req.get().session().path("a/cap");
		for(String s:basedir.list())basedir.get(s).rm();
		final path csrc=basedir.get("main.c");
		csrc.writestr("");
		if("b".equals(bt.toString()))
			b.b.cp($.class.getResourceAsStream("pco.c"),csrc.outputstream());
		cap.to(basedir.get("main.cap"));
		final Reader in=new StringReader(cap.toString());
		final Writer out=csrc.writer(true);
		out.write("// "+new Date()+"\n");
		out.flush();
		final String mode=bt.toString();
//		y.xu(c.clr());
		upd(y,c,true);
		final cap cc=new cap();
		try{
			cc.compile(in,out,mode.length()==0?"c":mode);
			y.xu(sts,new Date()+" ok");
			out.close();
			c.from(csrc);
			y.xu(c,true);
			upd_linenumbers(y,0);
			x_cc(y,null);
		}catch(Throwable t){
			out.close();
			c.from(csrc);
			y.xu(c);//? escape gtlt

			Throwable t1=t,t2;
			while(true){
				t2=t1.getCause();
				if(t2==null)break;
				t1=t2;
			}
			final String msg=t.getMessage();
			if(msg!=null){
				final int i=msg.indexOf("@(");
				if(i!=-1){
					final int i2=msg.indexOf(')',i+1);
					if(i2!=-1){
						final String rcs=msg.substring(i+2,i2);
						final String[]rc=rcs.split(":");
						final int lineno=Integer.parseInt(rc[0]);
						upd_linenumbers(y,lineno);
	//					System.out.println("rcx "+rc[0]+"   "+rc[1]);
					}
				}
			}
//			final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			y.xu(sts,new Date()+"\n "+error_line(t)+"\n\n\n stacktrace:\n"+stacktrace(t));
//			out.close();
//			c.from(csrc);
//			y.xu(c);
//			throw t;T
		}
	}
	private void upd_linenumbers(xwriter x, final int lineno) {
		final xwriter nx=new xwriter();//new line numbers
		for(int j=1;j<200;j++){
			if(j!=lineno){nx.p(j).nl();continue;}
			nx.p("<span style='background:brown;color:white;font-weight:bold'>").p(j).pl("</span>");
		}
		x.xu(ln,nx.toString());
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
	
	public static @conf String cc="gcc";
	public static @conf String gcc_opts_for_boot_image=" -o pco.img -nostdlib -Wl,--oformat,binary -Wl,-Ttext,0x7c00 -O0 -Wfatal-errors -Wno-int-to-pointer-cast main.c";
	public static @conf String gcc_opts_for_console=" -o main -std=c11 -Wfatal-errors main.c";
	public static boolean build_pco_boot_img=true;
	synchronized public void x_cc(xwriter x,String a)throws Throwable{
		final path basedir=req.get().session().path("a/cap");
		final path csrc=basedir.get("main.c");
		c.to(csrc);
		final xwriter y=x.xub(out,true,true);
		final String buildmode=bt.toString();
		try{
			final cli c=new cli("sh",new osnl(){@Override public void onnewline(final String line)throws Throwable{
				y.pl(line);
//			System.out.println(line);
			}}).p("date&&echo&&cd ").p(basedir.toString()).p("&&pwd&&echo&&");
			c.p(cc);
			if("b".equals(buildmode)){
//				b.cp(cap.class.getResourceAsStream("pc.c"),basedir.get("pc.c").outputstream());
//				b.cp(cap.class.getResourceAsStream("pc.h"),basedir.get("pc.h").outputstream());
				c.p(gcc_opts_for_boot_image);
			}else{
				c.p(gcc_opts_for_console);
			}
			c.p("&&ls -lA&&echo&&echo zipped word count&&cat main.cap|gzip|wc&&cat main.c|gzip|wc&&cat ").p("b".equals(buildmode)?"pco.img":"main").p("|gzip|wc");
			if(!"b".equals(buildmode))c.p("&&echo&&echo program output&&echo -- --- --- - - --- - -- - -- - - --- - - -- -  -&&./main&&echo&&echo -- - - - - -- -- - - - - ---  ---  -- - - - -----&&echo&&date");
			c.exit();
		}finally{x.xube();}
		x.xfocus(out);
	}
	
	private static final long serialVersionUID = 1L;
}
