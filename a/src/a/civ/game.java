package a.civ;
import java.io.*;
import static b.b.*;
import b.*;
public class game extends a{
	class unit extends a{}
	class tile extends a{}
	class player{}
	
	
//	/..\__/
//	\__/..\
//	/..\__/
//	\__/..\
	
	public void to(xwriter x)throws Throwable{
		x.pl("turn: "+turn+"\n\b");
		map.to(x);
		turn++;
	}
	
	private map map=new map();
	private int turn;
}
