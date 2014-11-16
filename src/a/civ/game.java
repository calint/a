package a.civ;
import java.util.ArrayList;
import java.util.List;

import b.a;
import b.xwriter;
public class game extends a{
	public void to(xwriter x)throws Throwable{
		m.to(x);
	}
//	public void xec(String cmd)throws Throwable{
//		try(final PushbackInputStream pis=new PushbackInputStream(new ByteArrayInputStream(cmd.getBytes()))){
//			final int c=pis.read();
//			if(c==-1)return;
//			if(c=='p'){m.put(pis);return;}
//			if(c=='t'){m.remove(pis);return;}
//			// qweasd
//			if(c=='m'){m.move(pis);return;}
//			if(c=='o'){//clear
//				turn=0;
//				m.clear();
//				m.put("a1",new tile("o "));
//				m.put("j8",new tile("x "));
//				return;
//			}
//		}
//		throw new Error("unknown command "+cmd);
//	}
	
	public List<player>players=new ArrayList<>();
	{
		players.add(new player(this,"player_1","player 1"));
		players.add(new player(this,"player_2","olayer 2"));
		players.add(new player(this,"player_3","nlayer 3"));
	}
	@Override protected a chldq(String nm){
		final player x=players.stream()
			.filter(u->nm.equals(u.nm()))
			.findAny()
			.get();
		if(x!=null)return x;
		return super.chldq(nm);
	}

	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(from instanceof player){
			b.b.pl("moves from "+from);
			final player p=(player)from;
			p.units.stream().forEach(u->{
				if(u.o.isempty())return;
				final String s=u.o.str();
				if(s.charAt(0)==' '){
					final char dir=s.charAt(1);
					// qwe
					// asd
					if(dir=='w'){
						return;
					}
					throw new Error();
				}
				if(u.old_o!=null)m.take(u.old_o);
				u.old_o=s;
				m.put(s,new tile(p.str().charAt(0)+u.str()));
			});
			
			player++;
			if(player>=players.size()){
				player=0;
				turn++;
			}
			super.ev(x,this);
			return;
		}
		super.ev(x,from,o);
	}
	
	
	public @readonly map m;//map
//	public @readonly player p;//player
	public @readonly int turn;
	public @readonly int player;

	public static @interface readonly{}
}
