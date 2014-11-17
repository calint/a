package a.civ;
import b.a;
import b.xwriter;
public class unit extends a{
	public unit(a pt,String anm,String name){super(pt,anm,name);}
	@Override public void to(xwriter x)throws Throwable{
		x.p(str()).p(": ");
		x.p(old_o).p("-");
		x.inputText(o,this,"");
		x.style(o,"background:#030;width:4em");
		x.nl();
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{}

	String old_o;

	/**order*/public a o;
	/**order log*/public a l;
	
}
