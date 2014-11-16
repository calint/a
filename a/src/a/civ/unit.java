package a.civ;
import b.a;
import b.xwriter;
public class unit extends a{
	public unit(player pt,String anm,String name){super(pt,anm,name);}
	@Override public void to(xwriter x)throws Throwable{
		x.p(str()).p(": ");
		x.inputText(o,this,"");
		x.style(o,"border-bottom:1px dotted blue;width:4em");
//		x.spc(2).p(last_o);
		x.nl();
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{
//		last_o=str();
	}

//	String last_o;

	/** order */public a o=new a(this,"o");//orders for this turn
	public a l=new a(this,"l");//orders log
	
}
