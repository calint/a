package a.civ;
import java.io.ByteArrayInputStream;
import java.io.PushbackInputStream;

import b.a;
import b.xwriter;
public class game extends a{
	public void to(xwriter x)throws Throwable{
		m.to(x);
	}
	public void xec(String cmd)throws Throwable{
		try(final PushbackInputStream pis=new PushbackInputStream(new ByteArrayInputStream(cmd.getBytes()))){
			final int c=pis.read();
			if(c==-1)return;
			if(c=='p'){m.put(pis);return;}
			if(c=='t'){m.remove(pis);return;}
			// qweasd
			if(c=='m'){m.move(pis);return;}
			if(c=='o'){//clear
				turn=0;
				m.clear();
				m.put("a1",new tile("o "));
				m.put("j8",new tile("x "));
				return;
			}
		}
		throw new Error("unknown command "+cmd);
	}
	public @readonly map m;//map
	public @readonly player p;//player
	public @readonly int turn;

	public static @interface readonly{}
}
