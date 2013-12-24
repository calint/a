package a.sokio.lib;
import b.*;
import static b.b.*;
public class soi extends a implements bin{
	private static final long serialVersionUID=1;
	public String contenttype(){return "text/plain;charset=utf8";}
	public void to(final xwriter x) throws Throwable{
		x.pl(" sokio").nl();
		recurse(x,path("src"));
	}
	private void recurse(final xwriter x,final path rootpath)throws Throwable{
		rootpath.foreach(new path.visitor(){public boolean visit(final path p)throws Throwable{
			if(p.isdir()){
				x.p("pe ").pl(p.name());
				recurse(x,p);
				x.pl("x");
			}else{
				x.p("oe ").pl(p.name());
				x.p("w ").pl(p.readstr().replaceAll("\\n","\\\\n"));//? p.to(new ossokio(x.outputstream()))
				x.pl("x");
			}
			return false;
		}});		
	}
}
