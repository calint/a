package a.civ;
import java.util.ArrayList;
import java.util.List;

import b.a;
import b.xwriter;
public class player extends a{
	public player(a apt,String anm,String name){super(apt,anm,name);}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
//		x.style(this,"border-bottom:1px dotted;padding:.2em;text-align:center;width:12em");
		x.nl().r(ul);
		x.ax(this,"s","::");
	}
	/**ordersforthisturn*/public a o;
	/**orderslog*/public a l;
	List<unit>units=new ArrayList<>();
	/**unitslist*/public listui ul;{ul.ls=units;}

	@Override protected a chldq(String id){
		if(id.startsWith("unit_")){
			final unit x=units.stream()
				.filter(u->id.equals(u.nm()))
				.findAny()
				.get();
			if(x!=null)return x;
		}
		return super.chldq(id);
	}
	
	public synchronized void x_(xwriter x,String a)throws Throwable{}
	public synchronized void x_s(xwriter x,String a)throws Throwable{
		ev(x,this);
	}




	{
		units.add(new unit(this,"unit_1","1"));
		units.add(new unit(this,"unit_2","2"));
		units.add(new unit(this,"unit_3","3"));
		units.add(new unit(this,"unit_4","4"));
	}
}
