package a.a;
import b.a;
import b.req;
import b.xwriter;
public class bits extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.pl("package names that end with .a require second bit in session bits.");
		x.pl("current session bits: ").p(req.get().session().bits());
	}
}
