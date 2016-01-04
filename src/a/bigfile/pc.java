package a.bigfile;

import java.util.Arrays;
import java.util.Collections;

import b.a;
import b.xwriter;

public class pc extends a{
	public storage[]s;
	public a inp,q,r;
	public pc() {
		s=new storage[2];
		for(int i=0;i<s.length;i++){
			s[i]=new storage(this,Integer.toString(i));
		}
	}
	@Override public void to(xwriter x)throws Throwable{
		x.style(inp,"border:1px dotted green;padding:.5em;width:30em;background:green");
		x.style(q,"border:1px dotted yellow;background:yellow;padding:.5em;width:10em");
		x.style(r,"border:1px dotted blue;display:block;padding:.5em;width:60em");
		x.style(r,"li","padding:.5em;width:100%");
		x.p("     search: ").inptxt(q,this,"q");
		x.focus(q);
		x.p("    or    typealine ").inptxt(inp,this,"add").nl();
		x.el(r).el_();
		x.nl().nl().nl();
		long n=0;
		for(final storage st:s)
			n+=st.size_in_bytes();
		x.p ("  pc has ").p(s.length).p(" storage device").p(s.length!=1?"s":"").p(", ").p_data_size(n).p(" bytes total");
		x.table("b");
		x.tr();
		for(final storage st:s){
			x.td().el(st).r(st).el_();
		}
		x.table_();
	}
	private int round_robin;
	synchronized public void x_add(xwriter x,String s)throws Throwable{
//		x.xu(inp);
		round_robin=++round_robin%this.s.length;
		final String data=inp.str();
		this.s[round_robin].x_add(x,data);
//		x.xu(this.s[round_robin]);
		inp.clr();
		x.xu(this);
		x.xfocus(inp);
	}
	synchronized public void x_q(xwriter x,String p)throws Throwable{
		final String qy=q.str();
		final String dest_elem_id=r.id();
		x.p("$s('").p(dest_elem_id).pl("','<ul>');");
		Arrays.stream(s,0,s.length).parallel().forEach(st->{try{
			st.query(qy,s->x.p("$p('").p(dest_elem_id).p("','<li>").p(s).pl("');"));
		}catch(Throwable t){throw new Error(t);}finally{}});
		x.p("$p('").p(dest_elem_id).pl("','</ul>');");
		q.clr();
		x.xu(q);
		x.xfocus(q);
	}
}
