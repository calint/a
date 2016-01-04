package a.bigfile;

import java.util.Arrays;

import b.a;
import b.req;
import b.xwriter;

public class pc extends a{
	private storage[]s;
	@Override protected a chldq(String nm){
		try{final int i=Integer.parseInt(nm);return s[i];}catch(NumberFormatException e){}
		return super.chldq(nm);
	}
	public a inp;//typealine input
	public a q;//query
	public a r;//query result display element
	public a st;//storage total bytes
	public pc()throws Throwable{
		s=new storage[2];
		for(int i=0;i<s.length;i++){
			s[i]=new storage_file(this,Integer.toString(i));
		}
		update_total_bytes_field();
	}
	private void update_total_bytes_field()throws Throwable{
		st.set(used_space_in_bytes());
	}
	public long used_space_in_bytes()throws Throwable{
		long n=0;
		for(final storage st:s)
			n+=st.used_space_in_bytes();
		return n;
	}
	@Override public void to(xwriter x)throws Throwable{
		x.style(inp,"border:1px dotted green;padding:.5em;width:30em");
		x.style(q,"border:1px dotted yellow;background:yellow;padding:.5em;width:10em");
		x.style(r,"border:0px dotted blue;display:block;padding:.5em;width:60em");
		x.style(r,"li","padding:.5em;padding-left:0;margin:0px;width:50em;border-bottom:1px dashed gray;list-style-type:none");//display:inline
		x.style(r,"li:first-child","border-top:3px solid red");
		x.style(r,"ul","border-top:3px double gray;width:51em;list-style:none;list-style-type:none");
		x.p("     search: ").inptxt(q,this,"q");
		x.focus(q);
		x.p("    or    typealine ").inptxt(inp,this,"add").nl();
		x.el(r);
		x.script();
		x_q(x,"");
		x.script_();
		x.el_();
		x.nl().nl().nl();
		x.spc(3).p(s.length).p(" storage device").p(s.length!=1?"s":"");
		x.p(", used total ").rel(st).pl(" bytes").nl();
		x.table("b");
		x.tr();
		for(final storage st:s){
			x.td().el(st).r(st).el_();
//			x.tr().td().el(st).r(st).el_();
		}
		x.table_();
	}
	@Override protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(from instanceof storage){
			if("storage.add".equals(o)){
				update_total_bytes_field();
				x.xu(st);
			}else throw new Error("unknown event object: "+o);
		}else super.ev(x,from,o);
	}
	private int round_robin;
	private void round_robin_update(){
		round_robin=++round_robin%this.s.length;
	}
	private void round_robin_update2()throws Throwable{
		int counter=0;
		int storage=0;
		long min_bytes=Long.MAX_VALUE;
		for(storage s:this.s){
			final long n=s.used_space_in_bytes();
			if(min_bytes>n){
				storage=counter;
				min_bytes=n;
			}
			counter++;
		}
		round_robin=storage;
	}
	synchronized public void x_add(xwriter x,String s)throws Throwable{
		round_robin_update2();
		final String data=inp.str();
		this.s[round_robin].x_add(x,data);
//		x.xp(this.s[round_robin],data);
		x.xu(this.s[round_robin]);
		inp.clr();
		x.xu(inp);
		x_q(x,q.str());
	}
	synchronized public void x_q(xwriter x,String p)throws Throwable{
		final String dest_elem_id=r.id();
		final String qy=q.str();
		if(qy.length()==0){
			x.p("$s('").p(dest_elem_id).pl("','');");
			return;
		}
		x.p("$s('").p(dest_elem_id).pl("','<ul>');");
		final req r=req.get();
		Arrays.stream(s,0,s.length).parallel().forEach(st->{try{
			st.query(r,qy,s->x.p("$p('").p(dest_elem_id).p("','<li>").jsstr(s).pl("');"));
		}catch(Throwable t){throw new Error(t);}finally{}});
		x.p("$p('").p(dest_elem_id).pl("','</ul>');");
//		q.clr();
//		x.xu(q);
//		x.xfocus(q);
	}
	private static final long serialVersionUID=1;
}
