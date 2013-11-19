package a.pics;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class $ extends a{static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.el(this);
		final long t0_ns=System.nanoTime();
		final req r=req.get();
		final path p=r.session().path(r.query());
		p.visit(new path.visitor(){public void visit(final path p){
			final String s=p.uri();
			if(s.endsWith(".jpg")||s.endsWith(".png"));
				x.tago("img").attr("src",s).attr("onkeydown","alert('d')").tagoe();
		}});
		final long dt_ns=System.nanoTime()-t0_ns;
		x.p(dt_ns).spc().p("ns").spc().nl();
		x.elend();
	}
}
