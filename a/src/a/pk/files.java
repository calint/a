package a.pk;
import a.x.cli;
import b.a;
import b.osnl;
import b.req;
import b.xwriter;
final public class files extends a{
	private static final long serialVersionUID=1;
	public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String[] qs=r.query().toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		x.p("<pre><code><tt>");
		//x.p(qs).nl();
		new cli("sh",new osnl(){public void onnewline(final String line){
				String s=line.toLowerCase().trim();
				x.a(s).p(s).a_().nl();
		};}).p("dpkg-query -L ").p(qs[0]).exit();
	}
}
