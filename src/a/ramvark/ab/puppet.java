package a.ramvark.ab;
import a.ramvark.an;
import a.ramvark.in;
import a.ramvark.itm;
import b.xwriter;
public class puppet extends itm{static final long serialVersionUID=1;
	@in(type=in.TYPE_AGGR_N,cls=expense.class,lst=expenses.class)public expenses expenses;
	@in(type=in.TYPE_AGGR_N,cls=item.class,lst=items.class)public items items;
	@in(type=in.TYPE_AGGR_1,cls=content.class)public agr content;
	@in(type=in.TYPE_STR)public an phone;	
	@in(type=in.TYPE_STR)public an email;	
	@in(type=in.TYPE_REF_1,cls=puppet.class,lst=ramvark.class)public ref referer;
	@in(type=in.TYPE_REF_N,cls=puppet.class,lst=ramvark.class)public refn refs;
	
	public final void itm_content_in(final xwriter x)throws Throwable{
		inputagr(x,content);
		x.spc();
		if(!content.isnull()){
			final content m=(content)content.get();
			final String s=m.body.toString();
			final int len=256;
			final String ss=s.length()<len?s:(s.substring(0,len)+"...");
			x.p(ss);
			x.spc().ax(this,"agrclr "+content.nm(),"x");
		}
	}
}
