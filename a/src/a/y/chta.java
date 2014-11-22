package a.y;

import java.io.Serializable;
import java.util.LinkedList;
import b.a;
import b.b;
import b.osltgt;
import b.req;
import b.xwriter;
public final class chta extends a{
	static final long serialVersionUID=1;

	private static LinkedList<pk>q=new LinkedList<pk>();
	private static long qt;
	private static void i(final i i){synchronized(q){for(final pk p:q)i.x(p);}}
	private interface i{void x(final pk m);}
	private final static class pk implements Serializable{
		static final long serialVersionUID=1;
		public final String frm;
		public final long ts;
		public final String s;
		public pk(final String s){
			frm=req.get().session().id();
			ts=System.currentTimeMillis();
			this.s=s;
			synchronized(q){
				if(ts>qt)
					qt=ts;
				q.addFirst(this);
			}
		}
		public String from(){return frm;}
		public long time(){return ts;}
		public String toString(){return s;}
	}
	
	public a inp;
	public a dsp;
	private int s=0;
	private long t=0;
	public void to(final xwriter x)throws Throwable{
		x.pre().inptxt(inp,this,"a","nbr line").spc().ax(this,"b","⊚").ax(this,"c","⊙").nl().span(dsp);
		x.script();
		x_a(x,null);
		x.xfocus(inp);
		x.xinterval(this,"e",5000);
		x.script_();
	}
	public void x_a(final xwriter x,final String s)throws Throwable{
		final String inps=inp.toString();
		if(!b.isempty(inps)){
			new pk(inps.toString());
			x.xu(inp.clr());
		}
		//ax_d(x,null);
	}
	public void x_b(final xwriter x,final String s){
		synchronized(q){q.clear();qt=0;}x.xfocus(inp);
	}
	public void x_c(final xwriter x,final String q){
		s=s==0?1:0;
		x_d(x,null);
	}
	public void x_d(final xwriter x,final String q){
		final xwriter y=new xwriter(new osltgt(x.xub(dsp,true,false).outputstream()));
//		final xwriter y=new xwriter(x.xub(dsp));
		i(new i(){public void x(final pk p){
			if(s==0)
				y.p(p.toString()).nl();
			else
				y.p(b.tolastmodstr(p.time())).spc().spc().a("/u/"+p.from(),p.from()).spc().spc().spc().spc().p(p.toString()).nl();
		}});
		y.flush();
		x.xube();
	}
	public void x_e(final xwriter x,final String s){
		if(qt!=0&&qt<=t)
			return;
		t=qt;
		x_d(x,null);
	}
}
