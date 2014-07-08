package a.budget;
import b.*;
import static b.b.*;
public class report extends a{
	static final long serialVersionUID=1;
	public a l;
	final public void to(final xwriter x)throws Throwable{
		x.pl(getClass().toString());
		x.nl();
		x.el(l);
		try{path().to(x);}catch(final Throwable t){x.pl(stacktraceline(t));}
		x.el_();
	}
	private path path(){
		return req.get().session().path(getClass().getPackage().getName()).get("log");
	}
}
