package a.pk;
import a.x.cli;
import b.a;
import b.osnl;
import b.req;
import b.xwriter;
public class requires extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String[]qs=r.query().toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		if(qs[0].length()==0)
			return;
		final StringBuilder sb=new StringBuilder(r.path()).append("?");
		x.p("<pre><code><tt>").nl();
		final int sbl=sb.length();
		new cli("sh",new osnl(){public void onnewline(final String line){
			String s=line.toLowerCase();
			int i=s.lastIndexOf(":");
			if(i==-1)
				return;
			if(s.indexOf("depends:")==-1)
				return;
			s=s.substring(i+1).trim();
			sb.append(s);
			x.a(sb.toString()).p(s).a_().nl();
			sb.setLength(sbl);
		};}).p("apt-cache -i depends ").p(qs[0]).exit();
	}
}
