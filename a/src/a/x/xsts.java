package a.x;
import b.a;
import b.sts;
import b.xwriter;
final public class xsts implements sts{
	private xwriter x;
	private long update_intervall_ms;
	private long t0;
	private a sts;
	public xsts(final xwriter x,final a sts,final long update_intervall_ms){
		this.x=x;
		this.sts=sts;
		this.update_intervall_ms=update_intervall_ms;
	}
	public void setsts(final String s)throws Throwable{
		sts.set(s);
		final long t=System.currentTimeMillis();
		if(t-t0<update_intervall_ms)
			return;
		t0=t;
		flush();
	}
	public void flush()throws Throwable{x.xu(sts).flush();}
}
