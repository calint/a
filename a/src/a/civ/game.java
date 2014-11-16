package a.civ;
import java.io.ByteArrayInputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<player>players=new ArrayList<>();
	{
		players.add(new player(this,"player_john","john"));
	}
	@Override protected a chldq(String id){
		final player x=players.stream()
			.filter(u->id.equals(u.nm()))
			.findAny()
			.get();
		if(x!=null)return x;
		return super.chldq(id);
	}

	
	
	public @readonly map m;//map
//	public @readonly player p;//player
	public @readonly int turn;
	public @readonly int player;

	public static @interface readonly{}
}
