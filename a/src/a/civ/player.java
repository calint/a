package a.civ;
import b.a;
import b.xwriter;
public class player extends a{
	{set("player");}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
		x.style(this,"background:yellow;border-top:1px dotted blue;border-radius:1em;padding:.2em;text-align:center;width:12em");
		x.nl();
//		x.pl("  gold: 0   ");
		x.pl(" units: none");
//		x.pl("cities: none");
	}
	public a o;//orders for this turn
	public a l;//orders log
	
	
	public synchronized void x_(xwriter x,String a)throws Throwable{}
}
