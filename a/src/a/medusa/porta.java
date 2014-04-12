package a.medusa;
import java.nio.ByteBuffer;
import java.util.Date;

import b.req;
import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	synchronized final@Override protected void onopened()throws Throwable{
		plr=m.alloc_sprite_for_new_player();
		if(plr==null)throw new Exception("no free players available");
		scr=new screen(80,40);
		System.out.println(" medusa: player connected  "+req.get().ip()+"  "+plr);
		medusa_thread.interrupt();
	}
	private player plr;
	private screen scr;
	synchronized final@Override protected void onclosed()throws Throwable{
		if(plr==null)return;//? protocol issues
		m.on_player_closed_connection(plr);
		System.out.println(" medusa: player disconnected  "+req.get().ip()+"  "+plr);
		plr=null;
		scr=null;
	}
	synchronized protected void onmessage(final ByteBuffer bb) throws Throwable {
		final byte cmd=bb.get();
		if(cmd==0){//key
			final byte key=bb.get();
			String kc=""+Character.toLowerCase((char)key);
			plr.on_msg(kc,m);//? y dt 
		}else if(cmd=='2'){//reset
			m.rst();
		}
		m.draw(scr);
		scr.bb.rewind();
		endpoint_recv(new ByteBuffer[]{ByteBuffer.wrap("0".getBytes()),scr.bb},true);
		if(medusa_loop_sleep_ms!=0)try{Thread.sleep(medusa_loop_sleep_ms);}catch(InterruptedException ignored){}
	}
	
	
	
	
	/// medusa server
	public static long medusa_loop_sleep_ms=100;
	static medusa m=new medusa();
	static float dt;
	static Thread medusa_thread=new Thread("medusa"){
		@Override public void run(){
			long last_dt_ms=System.currentTimeMillis();
			while(medusa_thread_on){
				final long t_ms=System.currentTimeMillis();
				long dt_try=(t_ms-last_dt_ms);
				if(dt_try<=0)dt_try=1;// set dt=1ms
				last_dt_ms=t_ms;
				dt=dt_try/1000.f;
				m.update(dt);
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
		try{m.rst();}catch(Throwable t){throw new Error(t);}
//		mds.update(0);
		medusa_thread.start();
	}
	static boolean medusa_thread_on=true;
}
