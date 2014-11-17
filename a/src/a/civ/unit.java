package a.civ;
import b.*;
public class unit extends a{
	public unit(String name){set(name);}
	@Override public void to(xwriter x)throws Throwable{
		x.p(str()).p(":").inputText(o).nl();
	}
	String old_o;
	/**order*/public a o;
	private static final long serialVersionUID=1;
}
