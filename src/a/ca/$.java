package a.ca;

import java.util.HashMap;
import java.util.Scanner;

import b.a;
import b.xwriter;

public class $ extends a {
	public a cap;
	public a c;
	public a sts;
	@Override public void to(xwriter x) throws Throwable {
		x.style("html","padding-left:4em;padding-top:1em");
		x.style(cap,"border:1px dotted green;width:40em;height:20em");
		x.style(sts,"border:1px dotted red");
		x.style(c,"display:block;border:1px dotted blue;width:40em");
		x.pl("cap source");
		x.inputTextArea(cap).nl().ax(this).p(" compile to c ").el(sts).elend();
		x.el(c).elend();
	}
	synchronized public void x_(xwriter y,String a){
		y.xu(sts,"");
		final xwriter x=y.xub(c,true,true);
		x.pl("#include\"cap.h\"");
		x.pl("int cap_main(int argc,const char**arg){");
		x.pl("    while(argc--)puts(*arg++);");
		x.pl("\n///--- cap compiled\n");
		
//		//file f=new file(rom,sizeof(rom)-1);
//		//file f=file(rom,sizeof(rom)-1);
//		//file f=(rom,sizeof(rom)-1);
//		//file f(rom,sizeof(rom)-1);
//		f=file(rom,sizeof(rom)-1);
//		f.info(stdout);
		
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
				}else if(ln.startsWith("rel ")){
					final String var=ln.substring("rel ".length(),ln.length()-1);
					final String type=vars.get(var);
					if(type==null)throw new Exception("@"+lineno+": variable '"+var+"' is not declared yet");
					x.p(type).p("_free(").p(var).p(");").nl();
				}else if(ln.startsWith("call ")){
					final int i1=ln.indexOf('.');
					if(i1==-1)throw new Exception("@"+lineno+": expected i.e. call f.info(stdout)");
					final String var=ln.substring("call ".length(),i1);
					final String type=vars.get(var);
					if(type==null)throw new Exception("@"+lineno+": variable '"+var+"' is not declared yet");
					final String fa=ln.substring(i1+1);
					final int i2=fa.indexOf('(');
					if(i2==-1)throw new Exception("@"+lineno+": expected i.e. call "+var+".info(stdout)");
					final String fu=fa.substring(0,i2);
					final String arg=fa.substring(i2+1);
					x.p(type).p("_").p(fu).p("(").p(var).p(",").p(arg).nl();
				}else if(ln.startsWith("set ")){
					final int i1=ln.indexOf('=');
					if(i1==-1)throw new Exception("@"+lineno+": set const size_t s=d.size_in_bytes();");
					final String ccode=ln.substring("set ".length(),i1);
					final int i2=ln.indexOf('.',i1);
					if(i2==-1)throw new Exception("@"+lineno+": set const size_t s=d.size_in_bytes();");
					final String var=ln.substring(i1+1,i2);
					final String type=vars.get(var);
					if(type==null)throw new Exception("@"+lineno+": variable '"+var+"' is not declared yet");
					final int i3=ln.indexOf('(',i2);
					if(i3==-1)throw new Exception("@"+lineno+": set const size_t s=d.size_in_bytes();");
					final String fu=ln.substring(i2+1,i3);
					final String arg=ln.substring(i3+1).trim();
					x.p(ccode).p("=").p(type).p("_").p(fu).p("(").p(var);
					if(arg.length()>2){x.p(",");}
					x.p(arg).nl();
				}else{
					x.pl(lnnotri);
				}
			}
//			cap.to(x);
		}catch(Throwable t){
			y.xube().xu(sts,t.getMessage());
			return;
		}
		x.pl("\n///--- cap compiled done\n");
//
//		    file*f=file_new(rom,sizeof(rom)-1);
//		    pl("files %lu",file_count);
//		    file_info(f,stdout);nl();
//		    file_to(f,stdout);nl();
//		    __block char chh='.';
//		    file_foreach_char_write(f,^(char*ch){
//		        putchar(*ch);
//		        *ch='x';
//		        putchar(chh++);
//			});nl();
//		    file_to(f,stdout);nl();
//		    file_copy(f,"another",3);
//		    file_to(f,stdout);nl();
////		    vfunc(f,info, stdout);
//		    file_recycle(f);
//		    pl("files %lu",file_count);
//
		x.pl("    return 0;");
		x.pl("}");
		x.pl("");
		y.xube();
	}
	
	
	
	private static final long serialVersionUID = 1L;
}
