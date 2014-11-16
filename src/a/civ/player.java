package a.civ;
import b.a;
import b.xwriter;
public class player extends a{
	{set("player");}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
		x.style(this,"border:1px dotted blue;border-radius:.5em;padding:.5em;text-align:center;width:7em");
//		x.pl(pt().getClass().getName()+": "+str());
	}
	public a o;//orders for this turn
	public a l;//orders log
	
	
	public synchronized void x_(xwriter x,String a)throws Throwable{}
}
