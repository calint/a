package a.civ;
import b.a;
import b.a_ajaxsts;
import b.xwriter;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.style("html","text-align:center;padding:1em 0 0 0;width:40em");
		x.style("html","background:#222;color:#0c0");
		x.style("a","color:#0c0");
		x.span("font-size:2em").p("con civ").span_().nl(2);
		x.pl("  _  ___  ||\\\\ ||");
		x.pl("//  || || || \\\\||");
		x.spano(m);
		x.r(h);
		x.nl().spano(s);
		x.nl().spano(p).style(p,"display:block");
		x.nl(2).r(ajaxsts).nl();
		x.style(ajaxsts,"font-size:.5em");
		x.script();x_(x,"");x.script_();
	}

	public synchronized void x_(xwriter x,String a)throws Throwable{
		g.m.to(x.xub(m,true,false));x.xube();
		g.players.get(g.player).to(x.xub(p,true,false));x.xube();
		x.xu(s,"turn "+g.turn);
//		x.xu(inp.clr());
	}

	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		b.b.pl("event from "+from.getClass());
		if(from instanceof game){
			x_(x,"");
			return;
		}
		super.ev(x,from,o);
	}

	public game g;
	/**mapoutput*/public a m;
	/**playeroutput*/public a p;
	/**statusoutput*/public a s;
	/**builtinajaxstatusline*/public a_ajaxsts ajaxsts;
	/**cheatsheet*/public hlp h;

	static final long serialVersionUID=1;
}