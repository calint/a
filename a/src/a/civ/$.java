package a.civ;
import b.a;
import b.a_ajaxsts;
import b.xwriter;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.style("html","text-align:center;padding:1em 0 0 0;width:40em");
		x.spano(m);

//		x.inputText(inp,this,"");
//		x.focus(inp);
//		x.style(inp,"border-bottom:1px dotted green;width:5em;padding:.5em;border-radius:.5em");
//		x.ax(this).nl(2);

		x.nl();
		x.spano(p);x.style(p,"display:block");
		x.nl(2).r(ajaxsts).nl();
		x.style(ajaxsts,"font-size:.5em");


		x.script();x_(x,"");x.script_();
	}

	public synchronized void x_(xwriter x,String a)throws Throwable{
		g.xec(inp.str());
		
		g.m.to(x.xub(m,true,false));x.xube();
		g.players.get(g.player).to(x.xub(p,true,false));x.xube();
		
		x.xu(inp.clr());
	}

	public game g;
	public a m;//map
	public a p;//player
	public a inp;//input


	public a_ajaxsts ajaxsts;//system supplied ajax status line






	static final long serialVersionUID=1;
}