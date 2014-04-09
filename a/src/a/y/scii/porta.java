package a.y.scii;
import java.nio.ByteBuffer;
import java.util.Date;

import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
{
	System.out.println("porta opened");}
	final@Override protected void onopened()throws Throwable{
		plr=mds.alloc_sprite_for_new_player();
		if(plr==null)throw new Exception("no free players available");
		medusa_thread.interrupt();
	}
	private sprite plr;
	final@Override protected void onclosed()throws Throwable{
		mds.on_player_closed_connection(plr);
	}
	synchronized protected void onmessage(final ByteBuffer bb) throws Throwable {
		final byte cmd=bb.get();
		if(cmd==0){//key
			final byte key=bb.get();
			String kc=""+Character.toLowerCase((char)key);
			plr.on_msg(kc);
		}else if(cmd=='2'){//reset
			mds.rst();
		}
		mds.draw();
		mds.scr.bb.rewind();
		endpoint_recv(new ByteBuffer[]{ByteBuffer.wrap("0".getBytes()),mds.scr.bb},true);
		if(medusa_loop_sleep_ms!=0)try{Thread.sleep(medusa_loop_sleep_ms);}catch(InterruptedException ignored){}
	}
	public static long medusa_loop_sleep_ms=100;
	static medusa mds=new medusa();
	static Thread medusa_thread=new Thread(new Runnable(){@Override public void run(){
		while(medusa_thread_on){
			mds.update();
			if(medusa_loop_sleep_ms!=0)try{Thread.sleep(medusa_loop_sleep_ms);}catch(InterruptedException ignored){}
			while(mds.sprites_available_for_new_players.size()==mds.sprites.size()){
				System.out.println(new Date()+": medusa: no players active, sleeping");
				try{Thread.sleep(24*60*60*1000);}catch(InterruptedException ok){}
				System.out.println(new Date()+": medusa: wakeup");
			}
		}
	}},"medusa");
	static{
		mds.rst();medusa_thread.start();}
	static boolean medusa_thread_on=true;
}
