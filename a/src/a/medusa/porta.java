package a.medusa;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import b.req;
import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	synchronized final@Override protected void onopened()throws Throwable{
		plr=m.alloc_player();
		if(plr==null)throw new Exception("no free players available");
		scr=new screen(80,40);
		System.out.println(" medusa: player connected  "+req.get().ip()+"  "+plr);
		medusa_thread.interrupt();
	}
	private player plr;
	private screen scr;
	synchronized final@Override protected void onclosed()throws Throwable{
		if(plr==null){b.b.log(new IllegalStateException());return;}//? protocol issues
		m.on_closed_connection(plr);
		System.out.println(" medusa: player disconnected  "+req.get().ip()+"  "+plr);
		plr=null;
		scr=null;
	}
	synchronized protected void onmessage(final ByteBuffer bb)throws Throwable{
		final long timestamp_ms=System.currentTimeMillis();
		final String s=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
//		System.out.println(s);
		plr.on_msg(s,m);
		if(s.charAt(0)!='0')return;//only redraw when keys are sent
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss.sss");
		final String msg=sdf.format(new Date(timestamp_ms))+"  players "+m.players_active_count();
		final byte[]ba_msg=msg.getBytes();
		endpoint_recv(new ByteBuffer[]{ByteBuffer.wrap("1".getBytes()),ByteBuffer.wrap(ba_msg)},true);
		m.draw(scr);
		scr.bb.rewind();
		endpoint_recv(new ByteBuffer[]{ByteBuffer.wrap("0".getBytes()),scr.bb},true);
		if(medusa_loop_sleep_ms!=0)try{Thread.sleep(medusa_loop_sleep_ms);}catch(InterruptedException ignored){}
	}
	
	
	/////
	/// medusa server
	public static byte[]hello="medusa".getBytes();
	public static long medusa_loop_sleep_ms=100;
	private static medusa m=new medusa();
	private static float dt;
	private static Thread medusa_thread=new Thread("medusa"){
		@Override public void run(){
			long last_dt_ms=System.currentTimeMillis();
			while(medusa_thread_on){
				final long t_ms=System.currentTimeMillis();
				long dt_try=(t_ms-last_dt_ms);
				if(dt_try<=0)dt_try=1;// set dt=1ms
				last_dt_ms=t_ms;
				dt=dt_try/1000.f;
				m.tick(dt);
//				System.out.println("medusa dt "+dt);
				if(medusa_loop_sleep_ms!=0)try{Thread.sleep(medusa_loop_sleep_ms);}catch(InterruptedException ignored){}
				while(m.has_active_players()){
					System.out.println(new Date()+": medusa: no players active, sleeping");
					try{Thread.sleep(24*60*60*1000);}catch(InterruptedException ok){}
					System.out.println(new Date()+": medusa: wakeup");
				}
			}
		}
	};
	static{
		try{m.reset();}catch(Throwable t){throw new Error(t);}
		medusa_thread.start();
	}
	static boolean medusa_thread_on=true;
}
