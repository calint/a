package a.civ;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import b.a;
import b.xwriter;
public class game extends a{
	public void to(xwriter x)throws Throwable{
		map.to(x);
		turn++;
		x.pl("turn: "+turn+"\n\b");
	}
	public void exec(String cmd)throws Throwable{
		try(final InputStream is=new ByteArrayInputStream(cmd.getBytes())){
			final int c=is.read();
			if(c==-1)return;
			if(c=='p'){//put
				final int row=is.read()-'a';
				final int col=is.read()-'1';
				map.put(row,col,"o ");
				return;
			}
			if(c=='r'){//remove
				final int row=is.read()-'a';
				final int col=is.read()-'1';
				map.remove(row,col);
				return;
			}
		}
		throw new Error("unknown command "+cmd);
	}

	
	private map map=new map();
	private int turn;
}
