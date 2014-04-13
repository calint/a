package a.ca;

import java.util.HashMap;
import java.util.Scanner;

import a.x.cli;
import b.a;
import b.b;
import b.osnl;
import b.path;
import b.xwriter;

public class $ extends a {
	public a cap;// cap source input
	public a c;// c source compiled output
	public a sts;// compiler status
	public a out;// stdout at run
	{cap.from($.class.getResourceAsStream("sample.cap"),"// cap code");}
	@Override public void to(xwriter x) throws Throwable {
		x.title("cap c sandbox");
		x.style("html","padding:1em 4em 0 4em");
		x.style(cap,"background:#f8f8f8;border:1px dotted green;width:30em;height:128em;padding:0 1em 0 .5em");
		x.style(c,"background:#e8e8e8;display:block;border:1px dotted grey;width:30em;height:128em;padding:0 1em 0 .5em");
		x.style(sts,"border:1px dotted red;padding:.5em;background:yellow");
		x.style(out,"display:block;background:black;color:green;border:1px dotted grey;width:60em;height:128em;padding:0 1em 0 .5em");
		x.style("div.la","width:1024em");
		x.style("div.la div.c1","float:left;text-align:right;border:1px dotted grey;background:#f0f0f0;float:left;text-align:right;padding:0 .25em 0 .5em");
		x.style("div.la div.c2","float:left");
		x.style("div.la div.c3","float:left");
		x.style("div.la div.c4","float:left;text-align:rightl;border:1px dotted grey;background:#f0f0f0;float:left;text-align:right;padding:0 .25em 0 .5em");
		x.style("div.la div.c5","float:left");
		x.style("div.la div.c6","float:left");
		x.style("div.la div.c7","float:left");
		x.div("la");
			x.div("c1");for(int i=1;i<100;i++)x.pl(""+i);x.divEnd();
			x.div("c2").inputTextArea(cap).focus(cap).divEnd();
			x.div("c3").p(" cap ").ax(this).p(" to c ").el(sts).elend().divEnd();
			x.div("c4");for(int i=1;i<100;i++)x.pl(""+i);x.divEnd();
			x.div("c5").inputTextArea(c).divEnd();
			x.div("c6").p(" :: ").ax(this,"cc","run ").divEnd();
			x.div("c7").el(out).p("console output").elend().divEnd();
			x.pl("<br style=clear:left />");
		x.divEnd();
	}
	synchronized public void x_(xwriter y,String a)throws Throwable{
		y.xu(sts,"");
//		final xwriter x=y.xub(c,true,true);
		final xwriter x=new xwriter();
		x.pl("#include\"cap.h\"");
		x.pl("int cap_main(int argc,const char**arg){");
		x.pl("    while(argc--)puts(*arg++);");
		x.pl("\n///--- cap compiled\n");		
		int lineno=0;
		final HashMap<String,String>vars=new HashMap<>();
		try(final Scanner sc=new Scanner(cap.toString())){
			while(sc.hasNextLine()){
				lineno++;
				final String lnnotri=sc.nextLine();
				final String ln=lnnotri.trim();
				if(ln.startsWith("//"))continue;
				if(ln.startsWith("let ")){
					final int i1=ln.indexOf("=");
					if(i1==-1)throw new Exception("@"+lineno+": expected i.e. let x=file(0,1)");
					final String var=ln.substring("let ".length(),i1);
					final int i2=ln.indexOf("(",i1);
					if(i2==-1)throw new Exception("@"+lineno+": expected i.e. let x=file(0,1)");
					final String type=ln.substring(i1+1,i2);
					final String args=ln.substring(i2);
					x.p(type).p("*").p(var).p("=").p(type).p("_new").p(args).nl();
					vars.put(var,type);
					continue;
				}else if(ln.startsWith("rel ")){
					final String var=ln.substring("rel ".length(),ln.length()-1);
					final String type=vars.get(var);
					if(type==null)throw new Exception("line "+lineno+": variable '"+var+"' is not declared yet");
					x.p(type).p("_free(").p(var).p(");").nl();
//					vars.remove(var);//? put in freed list for better error message
					continue;
				}else if(ln.startsWith("call ")){
					final int i1=ln.indexOf('.');
					if(i1==-1)throw new Exception("line "+lineno+": expected i.e. call f.info(stdout)");
					final String var=ln.substring("call ".length(),i1);
					final String type=vars.get(var);
					if(type==null)throw new Exception("line "+lineno+": variable '"+var+"' is not declared yet");
					final String fa=ln.substring(i1+1);
					final int i2=fa.indexOf('(');
					if(i2==-1)throw new Exception("line "+lineno+": expected i.e. call "+var+".info(stdout)");
					final String fu=fa.substring(0,i2);
					final String arg=fa.substring(i2+1);
					x.p(type).p("_").p(fu).p("(").p(var).p(",").p(arg).nl();
					continue;
				}else if(ln.startsWith("set ")){
					final int i1=ln.indexOf('=');
					if(i1==-1)throw new Exception("line "+lineno+": found no '='  expected i.e. set const size_t s=d.size_in_bytes();");
					final String ccode=ln.substring("set ".length(),i1);
					final int i2=ln.indexOf('.',i1);
					if(i2==-1)throw new Exception("line "+lineno+": found no '.'  expected i.e. set const size_t s=d.size_in_bytes();");
					final String var=ln.substring(i1+1,i2);
					final String type=vars.get(var);
					if(type==null)throw new Exception("line "+lineno+": variable '"+var+"' is not declared yet");
					final int i3=ln.indexOf('(',i2);
					if(i3==-1)throw new Exception("line "+lineno+": expected i.e. set const size_t s=d.size_in_bytes();");
					final String fu=ln.substring(i2+1,i3);
					final String arg=ln.substring(i3+1).trim();
					x.p(ccode).p("=").p(type).p("_").p(fu).p("(").p(var);
					if(arg.length()>2){x.p(",");}
					x.p(arg).nl();
					continue;
				}
				{//check if: ref.func(args)
					final int i1=ln.indexOf('.');
					if(i1!=-1){
						final String var=ln.substring(0,i1);
						final String type=vars.get(var);
						if(type!=null){
							final String fa=ln.substring(i1+1);
							final int i2=fa.indexOf('(');
							if(i2==-1)throw new Exception("line "+lineno+": expected i.e. "+var+".foo(bar);");
							final String fu=fa.substring(0,i2);
							final String arg=fa.substring(i2+1);
							x.p(type).p("_").p(fu).p("(").p(var).p(",").p(arg).nl();
							continue;
						}
					}
				}
				{//check if: ref=cls(args)
					final int i1=ln.indexOf('=');
					if(i1!=-1){
						final String var=ln.substring(0,i1);
						if(var.indexOf(' ')==-1&&!var.startsWith("*")){
							final int i2=ln.indexOf('(',i1+1);
							if(i2==-1)throw new Exception("line "+lineno+": expected i.e. "+var+"=foo(bar);");
							final String type=ln.substring(i1+1,i2);
							vars.put(var,type);
							final String rest=ln.substring(i2);
							x.p(type).p("*").p(var).p("=").p(type).p("_new").p(rest).nl();
//							x.p(type).p("_new(").p(var).p(",").p(rest).nl();
							continue;
						}
					}
				}
				x.pl(lnnotri);
			}
		}catch(Throwable t){
//			y.xube();
			y.xu(sts,t.getMessage());
			y.xu(c.set(x.toString()));			
			return;
		}
		x.pl("\n///--- cap compiled done\n");
		x.pl("    return 0;");
		x.pl("}");
		x.pl("");
//		y.xube();
		final path basedir=b.path("howto/c/ca");
		final path csrc=basedir.get("app.c");
		c.to(csrc);
		y.xu(sts,"ok");
		y.xu(c.set(x.toString()));			
	}
	public static String cc="clang";
	synchronized public void x_cc(xwriter x,String a)throws Throwable{
		final path basedir=b.path("howto/c/ca");
		final xwriter y=x.xub(out,true,true);
		try{final cli c=new cli("sh",new osnl(){@Override public void onnewline(final String line)throws Throwable{
			y.pl(line);
//			System.out.println(line);
		}}).p("cd ").p(basedir.toString()).p("&&ls -la&&cat *.h *.c|gzip|wc&&").p(cc).p(" -o main -Wall *.c&&./main").exit();}
		finally{x.xube();}
		x.xfocus(out);
	}
	
	private static final long serialVersionUID = 1L;
}
