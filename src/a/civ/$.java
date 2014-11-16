package a.civ;
import b.a;
import b.a_ajaxsts;
import b.xwriter;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.style("html","text-align:center;padding:1em 0 0 0;width:40em");
		x.el(this);
		x.inputText(inp,this,"");
		x.focus(inp);
		x.style(inp,"border:1px dotted green;width:5em;padding:.5em;border-radius:.5em");
		x.ax(this).nl(2);
		x.output_holder(p);
//		x.style(p,"float:right");
		x.output_holder(m);
//		x.style(m,"float:left");
		x.script();x_(x,"");x.script_();
		
		x.nl(2).r(ajaxsts).nl();
		x.style(ajaxsts,"font-size:.5em");
	}

	public synchronized void x_(xwriter x,String a)throws Throwable{
		g.xec(inp.str());
		
		g.m.to(x.xub(m,true,false));x.xube();
		g.p.to(x.xub(p,true,false));x.xube();
		
		x.xu(inp.clr());
	}

	public game g;
	public a m;//map
	public a p;//player
	public a inp;//input


	public a_ajaxsts ajaxsts;//system supplied ajax status line






	static final long serialVersionUID=1;
}