package a.ramvark.ab;
import a.ramvark.cstore;
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
	@Override protected void onprerender()throws Throwable{
		final boolean b=!subexpenses.isempty();
		price.set_bit(0,b);
		amount.set_bit(0,b);
		unit.set_bit(0,b);
	}
	protected boolean validate(final xwriter x) throws Throwable{
		try{price.toint();}catch(final Throwable t){
			x.xalert("enter an integer").xfocus(price);
			return false;
		}
		return super.validate(x);
	}
	@Override protected void onpresave(final xwriter x)throws Throwable{//?
		recalc();
	}
	@Override protected void onaftersave(final xwriter x)throws Throwable{
		// cascade
		if(pid.isempty())
			return;
		itm im;
		//?
		try{im=cstore.load(expense.class,pid.toString());}catch(Throwable ignored){return;}
		if(!(im instanceof expense))
			return;
		expense e=(expense)im;
		e.recalc();
		cstore.save(e);
	}

	protected void recalc()throws Throwable{
		if(subexpenses.isempty())
			return;
		final class sum{int i;};
		final sum s=new sum();
		subexpenses.foreach(null,new store.visitor(){public void visit(final itm e)throws Throwable{
			final int i=((expense)e).price.toint();
			final int a=((expense)e).amount.toint();
			s.i+=i*a;
		}});
		price.set(s.i);
		amount.set(1);
	}
}
