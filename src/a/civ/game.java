package a.civ;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import b.a;
import b.xwriter;
public class game extends a{
	public void to(xwriter x)throws Throwable{
		m.to(x);
	}
	public void xec(String cmd)throws Throwable{
		try(final InputStream is=new ByteArrayInputStream(cmd.getBytes())){
			final int c=is.read();
			if(c==-1)return;
			if(c=='p'){//put
				final int col=is.read()-'a';
				final int row=is.read()-'1';
				final String s=(char)is.read()+" ";
				m.put(row,col,s);
				return;
			}
			if(c=='r'){//remove
				final int col=is.read()-'a';
				final int row=is.read()-'1';
				m.remove(row,col);
				return;
			}
			// qwe
			// asd
			if(c=='m'){//move
				final int from_col=is.read()-'a';
				final int from_row=is.read()-'1';
				final String s=m.take(from_row,from_col);
				final int to_col=is.read()-'a';
				final int to_row=is.read()-'1';
				m.put(to_row,to_col,s);
				return;
			}
			if(c=='o'){//clear
				turn=0;
				m.clear();
				m.put(0,0,"a ");
				m.put(7,7,"b ");
				return;
			}
		}
		throw new Error("unknown command "+cmd);
	}

	
	public map m;//map
	private int turn;
	public player p;//player
}
