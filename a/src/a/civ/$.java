package a.civ;
import b.a;
import b.a_ajaxsts;
import b.xwriter;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.style("html","text-align:center;padding:1em 0 0 0;width:40em");
		x.spano(m);
		x.nl().spano(s);
		x.nl(2).spano(p).style(p,"display:block");
		x.nl(2).r(ajaxsts).nl();
		x.style(ajaxsts,"font-size:.5em");
		x.script();x_(x,"");x.script_();
	}

	public synchronized void x_(xwriter x,String a)throws Throwable{
		g.m.to(x.xub(m,true,false));x.xube();
		g.players.get(g.player).to(x.xub(p,true,false));x.xube();
		x.xu(s,"turn: "+g.turn);
//		x.xu(inp.clr());
	}

	public game g;
	public a m;//map
	public a p;//player
//	public a inp;//input
	public a s;//sts


	public a_ajaxsts ajaxsts;//system supplied ajax status line




	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		b.b.pl("event from "+from.getClass());
		if(from instanceof game){
			x_(x,"");
			return;
		}
		super.ev(x,from,o);
	}




	static final long serialVersionUID=1;
}