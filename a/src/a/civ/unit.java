package a.civ;
import b.a;
import b.xwriter;
public class unit extends a{
	public unit(player pt,String unit_name){super(pt,"",unit_name);}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
		x.style(this,"background:yellow;border-top:1px dotted blue;border-radius:1em;padding:.2em;text-align:center;width:12em");
		x.nl();
		x.pl(" next move: none");
	}
	public a o;//orders for this turn
	public a l;//orders log
	
	public synchronized void x_(xwriter x,String a)throws Throwable{}
}
