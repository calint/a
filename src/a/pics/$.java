package a.pics;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class $ extends a{static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
//		final long t0_ns=System.nanoTime();
		final req r=req.get();
		final path p=r.session().path(r.query());
		p.apply(new path.visitor(){public boolean visit(final path p){
			final String s=p.uri();
			if(!s.endsWith(".jpg")&&!s.endsWith(".png"))
				return false;
			x.tago("img").attr("src",s).tagoe();
			return false;
		}});
//		final long dt_ns=System.nanoTime()-t0_ns;
//		x.p(dt_ns).pl(" ns");
	}
}
