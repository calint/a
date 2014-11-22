package a.civ;
import b.*;
final public class ui extends a{
	public void to(final xwriter x)throws Throwable{
		x.r(g.m).divh(s).r(g.p).divh(ajaxsts).r(h).r(l).r(t);
	}
	public synchronized void x_(final xwriter x,final String a)throws Throwable{
		upd();
		x.xuo(g.m).xuo(g.p).xu(s);
	}
	private void upd(){
		g.p.mode_view_focus(g.ap);
		s.set("turn "+g.t);
	}
	@Override protected void ev(final xwriter x,a from,final Object o)throws Throwable{
		// next-player
		x_(x,"");
	}
	public game g;
	/**status*/public a s;
	public hud h;
	public logo l;
	public techs t;
	/**builtinajaxstatusline*/public a ajaxsts;
	private static final long serialVersionUID=1;

	{	g.p.add(new player("player 1"));
		g.p.add(new player("olayer 2"));
		g.p.add(new player("nlayer 3"));
		upd();
	}
}