package a.civ;
import java.util.ArrayList;
import java.util.List;

import b.a;
import b.xwriter;
public class player extends a{
	public player(a apt,String anm,String name){super(apt,anm,name);}
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"").style(this,"border-bottom:1px dotted black;padding:.2em;text-align:center;width:12em");
		x.nl();
		units.forEach(u->{try{u.to(x);}catch(Throwable t){throw new Error(t);}});
		x.ax(this,"s"," :: next");
	}
	public a o=new a(this,"o");//orders for this turn
	public a l=new a(this,"l");//orders log
	
	public List<unit>units=new ArrayList<>();
	@Override protected a chldq(String id){
		final unit x=units.stream()
			.filter(u->id.equals(u.nm()))
			.findAny()
			.get();
		if(x!=null)return x;
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
