package a.civ;
import b.*;
public class game extends a{
	public void to(xwriter x)throws Throwable{
		map.to(x);
		turn++;
		x.pl("turn: "+turn+"\n\b");
	}
	public void exec(String ln)throws Throwable{map.exec(ln);}

	
	private map map=new map();
	private int turn;
}
