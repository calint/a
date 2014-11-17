package a.civ;
import b.a;
import b.xwriter;
final public class game extends a{
	public void to(xwriter x)throws Throwable{m.to(x);}
	public alist<player>players;
	{
		players.add(new player("player 1"));
		players.add(new player("olayer 2"));
		players.add(new player("nlayer 3"));
	}
//	@Override protected a chldq(String nm){
//		if(nm.startsWith("player_")){
//			final player x=players.stream()
//				.filter(u->nm.equals(u.nm()))
//				.findAny()
//				.get();
//			if(x!=null)return x;
//		}
//		return super.chldq(nm);
//	}
	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(from instanceof player){
			b.b.pl("moves from "+from);
			player++;
			if(player>=players.size()){
				players.stream().forEach(p->{
					p.units_stream().forEach(oo->{
						final unit u=(unit)oo;
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
				});
				player=0;
				turn++;
			}
			super.ev(x,this);
			return;
		}
		super.ev(x,from,o);
	}
	public @readonly map m;
	public @readonly int turn;
	public @readonly int player;

	public static @interface readonly{}
	private static final long serialVersionUID=1;
}
