package a.pk;
import a.x.cli;
import b.a;
import b.osltgt;
import b.req;
import b.xwriter;
final public class info extends a{
	private static final long serialVersionUID=1;
	public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String[]qs=r.query().toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		x.p("<pre>").nl();
		if(qs[0].length()==0)
			return;
		new cli("sh",new osltgt(x.outputstream())).p("apt-cache show ").p(qs[0]).exit();
	}
}
