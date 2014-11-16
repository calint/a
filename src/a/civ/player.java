package a.civ;
import b.a;
import b.xwriter;
public class player extends a{
	{set("player");}
	@Override public void to(xwriter x)throws Throwable{
		x.pl(pt().getClass().getName()+": "+str());
	}
	public a o;//orders for this turn
	public a l;//orders log
}
