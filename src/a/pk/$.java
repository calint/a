package a.pk;
import b.a;
import b.b;
import b.req;
import b.xwriter;
public class $ extends a{
	private static final long serialVersionUID=1;
	public a q;
	public info info;
	public requires requires;
	public files files;
	public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		String qdec=r.query();
		final String[] qs=qdec.toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		if(!(qs[0].length()==0))
			q.set(qs[0]);
		x.p("<pre>package “").inputText(q,null,this,"upd").focus(q).p("” ");
		int i=qs.length>1?Integer.parseInt(qs[1]):0;
		a[] wta=new a[]{files,requires,info};
		for(int j=0;j<wta.length;j++){
			a wt=wta[j];
			if(i==j)
				x.p("  :: ").p(wt.nm());
			else
				x.p("  :: ").a(r.path()+"?"+b.urlencode(qs[0])+"?"+j).p(wt.nm()).aEnd();
		}
		x.nl();
		if(q.toString().length()==0)
			return;
		x.rend(wta[i]).nl().nl();
	}
	public void x_upd(xwriter x,String s) throws Throwable{
		x.p("location.href=location.pathname+'?"+b.urlencode(q.toString())+"';");
	}
}
