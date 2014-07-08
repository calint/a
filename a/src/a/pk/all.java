package a.pk;
import java.net.URLDecoder;
import a.x.cli;
import b.a;
import b.osnl;
import b.req;
import b.xwriter;
final public class all extends a{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String qs=URLDecoder.decode(r.query(),"utf8").toLowerCase().replace(';',' ').replace('&',' ').trim();
		if(qs.length()==0)
			return;
		x.p("<pre><code><tt>");
		new cli("sh",new osnl(){
			@Override public void onnewline(String line){
				String s=line.trim().toLowerCase();
				if(!s.startsWith(qs))
					return;
				x.p("<a href=\"").p(getClass().getName().substring(b.b.webobjpkg.length()).replace('.','/'));
				if(qs!=null)
					x.p("?").p(qs);
				x.p("\">").p(s).a_().nl();
			};
		}).p("apt-cache pkgnames ").p(qs).exit();
	}
}
