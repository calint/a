package a.y;
import b.a;
import b.b;
import b.path;
import b.req;
import b.xwriter;
public class blok extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		final path p=req.get().session().path();
		x.style();
		x.css("el","display:inline-block;margin:.25em;padding:0 1em 0 1em;box-shadow:0 0 .5em rgba(0,1,0,1);border-radius:0px");
		x.css("el:hover","font-weight:bold");
		x.style_();
		for(final String fn:p.list()){
			x.el();
			x.ax(this,"dsp "+b.urlencode(fn),fn);
			x.ax(this,"add "+b.urlencode(fn)," • ").nl();
			x.p(p.get(fn).size()).p(" B").nl();
			x.el_();
		}
	}
}
