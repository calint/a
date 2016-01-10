package a.tinitus;

import static b.b.pl;

import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;

public class port_tinitus extends websock implements threadedsock, Runnable{
	public long sleep_time_in_ms=250;
	public boolean running=true;
	@Override
	protected void onopened() throws Throwable {
		pl("porta opened");
		new Thread(this,"porta").start();
	}
	@Override public void run(){try{
		pl("porta running");
		final ByteBuffer bb=ByteBuffer.allocate(4096*4);
		long c=0;
		while(running) {
			final byte[]ba=bb.array();
			for(int i=0;i<ba.length;i++){
				ba[i]=(byte)(Math.sin(c/256.f)*128);
				c++;
			}
			send_binary(bb);
			bb.flip();
			try{Thread.sleep(sleep_time_in_ms);}catch(InterruptedException ignored){}
		}
		pl("porta thread done");
	}catch(Throwable t){throw new Error(t);}}


	@Override protected void onmessage(ByteBuffer bb) throws Throwable {
		final byte cmd=bb.get();
		pl("porta on message: "+cmd);
		if(cmd==48){//faster
			sleep_time_in_ms-=50;
			if(sleep_time_in_ms<0)
				sleep_time_in_ms=0;
			pl("  faster    "+sleep_time_in_ms+" ms sleep");
			return;
		}
		if(cmd==49){//slower
			sleep_time_in_ms+=50;
			if(sleep_time_in_ms>1000)
				sleep_time_in_ms=1000;
			pl("  slower    "+sleep_time_in_ms+" ms sleep");
		}
	}

	@Override public void onconnectionlost() throws Throwable {
		pl("porta connection lost");
		running=false;
		Thread.currentThread().interrupt();
	}
	@Override protected void onclosed() throws Throwable {
		pl("porta on closed");
		running=false;
		Thread.currentThread().interrupt();
	}
}
