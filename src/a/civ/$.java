package a.civ;
import b.*;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.style("html","background:#111");
		x.style("a","color:#0c0");
		x.div(this,null,"padding:5em;padding-top:0;background:#222;color:#0c0;text-align:center;display:table;margin-left:auto;margin-right:auto",null);
		x.divx(g.m).divx(s).nl().divx(g.p).nl().r(h).nl().r(l).nl().r(ajaxsts);
		x.style(ajaxsts,"font-size:.5em");
		x.script();x_(x,"");x.script_();
		x.div_();
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{
		x.xrfsh(g.m);
		g.p.mode_view_focus(g.ap);
		x.xrfsh(g.p);
		x.xu(s,"turn "+g.t);
	}
	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(from instanceof game){
			b.pl("event from game");
			x_(x,"");
			return;
		}
		super.ev(x,from,o);
	}
	public game g;{	
		g.p.add(new player("player 1"));
		g.p.add(new player("olayer 2"));
		g.p.add(new player("nlayer 3"));
	}
	/**status*/public a s;
	public hud h;
	public logo l;
	public a_ajaxsts ajaxsts;
	private static final long serialVersionUID=1;
}