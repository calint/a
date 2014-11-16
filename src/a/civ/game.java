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
				final int col=is.read()-'a';
				final int row=is.read()-'1';
				final String s=(char)is.read()+" ";
				map.put(row,col,s);
				return;
			}
			if(c=='r'){//remove
				final int col=is.read()-'a';
				final int row=is.read()-'1';
				map.remove(row,col);
				return;
			}
			// qwe
			// asd
			if(c=='m'){//move
				final int from_col=is.read()-'a';
				final int from_row=is.read()-'1';
				final String s=map.take(from_row,from_col);
				final int to_col=is.read()-'a';
				final int to_row=is.read()-'1';
				map.put(to_row,to_col,s);
				return;
			}
			if(c=='o'){//clear
				turn=0;
				map.clear();
				map.put(0,0,"a ");
				map.put(9,11,"b ");
				return;
			}
		}
		throw new Error("unknown command "+cmd);
	}

	
	private map map=new map();
	private int turn;
}
