package a.civ;
import static b.b.*;
import b.*;
public class game extends a{
	public void to(xwriter x)throws Throwable{
		map.to(x);
		turn++;
		x.pl("turn: "+turn+"\n\b");
	}
	
	private map map=new map();
	private int turn;
}
