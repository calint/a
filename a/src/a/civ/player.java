package a.civ;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import b.a;
import b.xwriter;
public class player extends a{
	public player(a apt,String anm,String name){super(apt,anm,name);}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
//		x.style(this,"border-bottom:1px dotted;padding:.2em;text-align:center;width:12em");
		x.nl();
		x.table().tr().td().r(ull).td().r(ul).td().r(ulr).table_();
		x.ax(this,"s","::");
	}
//	/**ordersforthisturn*/public a o;
//	/**orderslog*/public a l;
	
	
	/**leftunitslist*/public alist ull;{ull.ls=new ArrayList<>();}
	/**rightunitslist*/public alist ulr;{ulr.ls=new ArrayList<>();ulr.rht=ull;ull.lft=ulr;}
//	private List<unit>units=new ArrayList<>();
//	/**unitslist*/public alist ul;{ul.ls=units;ul.rht=ulr;ul.lft=ull;}
	/**unitslist*/public alist ul;{ul.ls=new ArrayList<>();ul.rht=ulr;ul.lft=ull;}
	{ulr.lft=ull.rht=ul;}

	public Stream units_stream(){
		final Stream s=Stream.concat(ul.ls.stream(),Stream.concat(ull.ls.stream(),ul.ls.stream()));
		return s;
	}
	public synchronized void x_(xwriter x,String a)throws Throwable{}
	public synchronized void x_s(xwriter x,String a)throws Throwable{ev(x);}

	{
		ul.ls.add(new unit(ul,"1","1"));
		ul.ls.add(new unit(ul,"2","2"));
		ul.ls.add(new unit(ul,"3","3"));
		ul.ls.add(new unit(ul,"4","4"));
	}
}
