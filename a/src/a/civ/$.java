package a.civ;
import static b.b.*;
import b.*;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.style("html","text-align:center;padding:1em 0 0 0");
		x.inputText(inp,this,"");
		x.focus(inp);
		x.style(inp,"border:1px dotted green;width:5em;padding:.5em;border-radius:.5em");
		x.ax(this).nl(2);
		x.output_holder(c);
		x.script();x_(x,"");x.script_();
		x.nl().r(ajaxsts).nl();
	}

	public synchronized void x_(xwriter x,String arg)throws Throwable{
		g.to(x.xub(c,true,false));
		x.xube();	
	}

	public a c;//console
	public game g;
	public a inp;//input


	public a_ajaxsts ajaxsts;//system supplied ajax status line






	static final long serialVersionUID=1;
}