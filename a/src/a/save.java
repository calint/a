package a;

import java.text.DecimalFormat;
import java.util.Date;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class save extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		final session s=req.get().session();
		final long t0=System.currentTimeMillis();
		s.save();
		final long dt=System.currentTimeMillis()-t0;
		final path sp=s.path(b.sessionfile);
		x.pre();
		x.p("    date: ").pl(new Date().toString());
		x.p(" session: ").pl(s.id());
		x.p("    path: ").pl(sp.toString());
		x.p("    size: ").p(new DecimalFormat("###,###,###,###").format(sp.size())).pl(" B");
		x.p("    time: ").p(dt).pl(" ms");
		x.p(" content: ").nl();
		for(final String key:s.keyset()){
			x.tab().p(key).p(" ").pl(s.get(key).toString());
		}
		x.nl().nl();
	}
}
