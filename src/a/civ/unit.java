package a.civ;
import b.*;
public class unit extends a{
	public unit(String name){set(name);}
	@Override public void to(xwriter x)throws Throwable{
//		x.div(this);
		x.p(str()).p(":");
		x.inputText(o,this,"");
		x.style(o,"background:#030;width:4em");
		x.nl();
//		x.div_();
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{}
	String old_o;
	/**order*/public a o;
	private static final long serialVersionUID=1;
}
