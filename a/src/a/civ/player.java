package a.civ;
import java.util.stream.Stream;

import b.a;
import b.xwriter;
final public class player extends a{
	public player(String name){set(name);}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
		x.style(this,"border-bottom:1px dotted;padding:.2em;text-align:center;width:24em");
		x.nl();
		x.table("","margin-left:auto;margin-right:auto").tr().td().r(ul).td().r(uc).td().r(ur).table_();
		x.ax(this,"s","::");
	}
	/**unitlistleft*/public alist<unit>ul;
	/**unitlistcenter*/public alist<unit>uc;
	/**unitlistright*/public alist<unit>ur;
	{ul.link_to_left_of(uc);ur.link_to_right_of(uc);ur.link_warp(ul);}

	public Stream<unit>units_stream(){return Stream.concat(uc.stream(),Stream.concat(ul.stream(),ur.stream()));}
	public synchronized void x_(xwriter x,String a)throws Throwable{}
	public synchronized void x_s(xwriter x,String a)throws Throwable{ev(x);}

	{
		uc.add(new unit("1"));
		uc.add(new unit("2"));
		uc.add(new unit("3"));
		uc.add(new unit("4"));
	}

	private static final long serialVersionUID=1;
}
