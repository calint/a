package a.civ;
import b.*;
final public class $ extends a{{upd();}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null){//if-root
			x.style("html","background:#111");
			x.style("body","");
			x.style("a","color:#0c0");
		}
		x.div(this,null,"border:1px dashed;border-radius:.5em;box-shadow:0 0 1em rgba(0,0,0,1);padding:5em;padding-top:0;background:#222;color:#0c0;text-align:center;display:table;margin-left:auto;margin-right:auto",null);
		x.r(g.m).divr(s).r(g.p).divr(ajaxsts).r(h).r(l).r(t);
		x.div_();
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{
		upd();
		x.xuo(g.m).xuo(g.p).xu(s);
	}
	private void upd(){
		g.p.mode_view_focus(g.ap);
		s.set("turn "+g.t);
	}
	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		// refresh-view
		x_(x,"");
	}
	public game g;{	
		g.p.add(new player("player 1"));
		g.p.add(new player("olayer 2"));
		g.p.add(new player("nlayer 3"));
	}
	/**status*/public a s;
	public hud h;
	public logo l;
	public techs t;
	/**builtinajaxstatusline*/public a ajaxsts;
	private static final long serialVersionUID=1;
}