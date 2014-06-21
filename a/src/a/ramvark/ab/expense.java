package a.ramvark.ab;
import a.ramvark.in;
import a.ramvark.itm;
import a.ramvark.store;
import b.a;
import b.xwriter;
public class expense extends itm{static final long serialVersionUID=1;
	@in(type=3)public expenses subexpenses;
	@in(type=5)public a unit;
	@in(type=5)public a amount;
	@in(type=5)public a price;
	protected boolean validate(final xwriter x) throws Throwable{
		try{price.toint();}catch(final Throwable t){
			x.xalert("enter an integer").xfocus(price);
			return false;
		}
		return super.validate(x);
	}
	protected void onpresave(final xwriter x)throws Throwable{//?
		if(subexpenses.isempty()){
			return;
		}
		final class sum{int i;};
		final sum s=new sum();
		subexpenses.foreach(null,new store.visitor(){public void visit(final itm e)throws Throwable{
			final int i=((expense)e).price.toint();
			s.i+=i;
		}});
		price.set(s.i);
		if(x!=null)x.xu(price);
	}
}
