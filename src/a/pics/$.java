package a.pics;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class $ extends a{
	static final long serialVersionUID=1;
	public a s;
	public void to(final xwriter x)throws Throwable{
		final long t0_ns=System.nanoTime();
		x.pre();
		x.pl("ĸoö: ");
		final req r=req.get();
		final path path=r.session().path(r.query());
		x.pl(path.uri());
		path.visit(new path.visitor(){public void visit(final path p){
			x.a(p.uri()).p(p.uri()).aEnd().nl();
		}});

		final long dt_ns=System.nanoTime()-t0_ns;
		x.p(dt_ns).spc().p("ns").spc().nl();
	}
}
