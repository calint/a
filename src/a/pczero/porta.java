package a.pczero;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	final private vintage v=new vintage();
	final protected void onopened()throws Throwable{
		session().put(getClass().getName(),v);
		v.x_l(null,null);
		v.x_c(null,null);
		v.x_r(null,null);
	}
	protected void onmessage(final ByteBuffer bb) throws Throwable {
		final byte cmd=bb.get();
		if(cmd==0){//key
			final byte key=bb.get();
			v.ram.set(0x0080*0x0100-1,key);
		}else if(cmd==49){//compile
//			System.out.println(cmd);
			final String src=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
//			System.out.println(src);
			v.src.txt.set(src);
			try{
				v.x_c(null,null);
			}catch(final Throwable t){
				final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1"+b.b.stacktrace(t)));
				send(bbe,false);
				return;
			}
			final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1"+v.sts.toString()));
			send(bbe,false);
			v.x_r(null,null);
		}
//		System.out.println(bb.remaining()+"  "+key);
		v.x_f(null,null);
		final long t0=System.currentTimeMillis();
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(32*b.b.K);
		v.snapshot(baos);
		final ByteBuffer[]bbpng=new ByteBuffer[]{ByteBuffer.wrap(new byte[]{0}),ByteBuffer.wrap(baos.toByteArray())};
		final long t1=System.currentTimeMillis();
		while(issending()){
			System.out.println(new Date()+"\t"+session().id()+"\tstaling");
			try{Thread.sleep(20);}catch(final InterruptedException ignored){}
		}
		send(bbpng,false);
		final long t2=System.currentTimeMillis();
	}
}
