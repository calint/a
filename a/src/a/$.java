package a;
import b.a;
import b.req;
import b.xwriter;
public class $ extends a{
	static final long serialVersionUID=1;
	public diro d;{d.root(req.get().session().path());d.bits(diro.BIT_ALL);}
	public void to(final xwriter x)throws Throwable{
		x.style().css("body","padding:0 10em 0 4em").styleEnd();
		d.to(x);
	}
}
