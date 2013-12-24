package a.sokio.lib;
import b.*;
public class $ extends a implements bin{
	private static final long serialVersionUID=1;
	public String contenttype(){return "text/plain;charset=utf8";}
	public void to(final xwriter x) throws Throwable{
		x.pl(" sokio").nl();
		final path rp=b.path(req.get().query());
		recurse(x,rp);
	}
	private void recurse(final xwriter x,final path rp)throws Throwable{
		rp.foreach(new path.visitor(){public boolean visit(final path p)throws Throwable{
			if(p.isdir()){
				x.p("pe ").pl(p.name());
				recurse(x,p);
				x.pl("x");
			}else{
				x.p("oe ").pl(p.name());
				x.p("w ").pl(p.readstr().replaceAll("\\n","\\\\n"));
				x.pl("x");
			}
			return false;
		}});		
	}
}
