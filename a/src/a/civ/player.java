package a.civ;
import b.a;
import b.xwriter;
public class player extends a{
	@Override public void to(xwriter x) throws Throwable {
		x.pl(getClass()+":"+this);
	}
	public a nm;//name
}
