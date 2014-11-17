package a.civ;
import b.*;
final public class game extends a{
	public void to(xwriter x)throws Throwable{m.to(x);}
	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(from instanceof player){
			b.pl("player "+from+" submitted moves");
			ap++;
			if(ap>=p.size()){
				b.pl("all players submitted moves. processing turn.");
				p.stream().forEach(p->{
					p.units_stream().forEach(u->{
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
						m.put_unit(s,u);
					});
				});
				ap=0;
				t++;
				super.ev(x,this);//end-of-turn
				return;
			}
			super.ev(x,from,o);//player-submitted-move
			return;
		}
		super.ev(x,from,o);
	}
	public map m;
	/**turn*/public int t;
	/**activeplayer*/public int ap;
	public alist<player>p;

	private static final long serialVersionUID=1;
}
