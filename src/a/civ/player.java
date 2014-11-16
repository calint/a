package a.civ;
import java.util.ArrayList;
import java.util.List;

import b.a;
import b.xwriter;
public class player extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.inputText(this,this,"");
		x.style(this,"background:yellow;border-top:1px dotted blue;border-radius:1em;padding:.2em;text-align:center;width:12em");
		x.nl();
//		x.pl("  gold: 0   ");
//		x.pl(" units: "+units);
		units.forEach(u->{try{u.to(x);}catch(Throwable t){throw new Error(t);}});
//		x.pl("cities: none");
	}
	public a o;//orders for this turn
	public a l;//orders log
	
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




	{
		set("john");
		units.add(new unit(this,"a"));
		units.add(new unit(this,"b"));
	}
}
