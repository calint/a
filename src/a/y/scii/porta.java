package a.y.scii;
import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	final protected void onopened()throws Throwable{
//		System.out.println("connected "+req.get().ip());
	}
	private int playerid=0;
	synchronized protected void onmessage(final ByteBuffer bb) throws Throwable {
		final byte cmd=bb.get();
		if(cmd==0){//key
			final byte key=bb.get();
			String kc=""+Character.toLowerCase((char)key);
//			System.out.println("key:"+kc);
			mds.sprites.get(playerid).on_msg(kc);
//			v.ram.set(0x0080*0x0100-1,key);
		}else if(cmd=='2'){//reset
			mds.rst();
		}else if(cmd=='1'){//compile
//			System.out.println(cmd);
//			final String src=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
			final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1"+"hello world"));
			endpoint_recv(bbe,false);
		}
//		System.out.println(bb.remaining()+"  "+key);
		mds.draw();
		mds.scr.bb.rewind();
		endpoint_recv(new ByteBuffer[]{ByteBuffer.wrap("0".getBytes()),mds.scr.bb},true);
		Thread.sleep(100);
//		final long t2=System.currentTimeMillis();
	}
	
	static medusa mds=new medusa();
	static Thread medusa_thread=new Thread(new Runnable(){@Override public void run(){
		while(on){
			mds.update();
//			mds.draw();
			try{Thread.sleep(100);}catch(InterruptedException ignored){}
		}
	}public boolean on=true;},"medusa");static{mds.rst();medusa_thread.start();}

}
