package a.civ;
import java.util.stream.Stream;

import b.a;
import b.xwriter;
public class player extends a{
	public player(a apt,String anm,String name){super(apt,anm,name);}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
		x.style(this,"border-bottom:1px dotted;padding:.2em;text-align:center;width:24em");
		x.nl();
		x.table("","margin-left:auto;margin-right:auto").tr().td().r(ul).td().r(uc).td().r(ur).table_();
		x.ax(this,"s","::");
	}
//	/**ordersforthisturn*/public a o;
//	/**orderslog*/public a l;
	
	
	/**unitslistleft*/public alist<unit>ul;
	/**unitslistcenter*/public alist<unit>uc;
	/**unitslistright*/public alist<unit>ur;
	{uc.link(ul,uc);uc.link(uc,ur);uc.link(ur,ul);}

	public Stream<unit>units_stream(){
		return Stream.concat(uc.stream(),Stream.concat(ul.stream(),ur.stream()));
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{}
	public synchronized void x_s(xwriter x,String a)throws Throwable{ev(x);}

	{
		uc.add(new unit(uc,"1","1"));
		uc.add(new unit(uc,"2","2"));
		uc.add(new unit(uc,"3","3"));
		uc.add(new unit(uc,"4","4"));
	}
	
	
		private static final long serialVersionUID = 1L;

}
