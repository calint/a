package a.pz;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	final private acore co;
	public porta()throws Throwable{
		co=new acore();
	}
	final protected void onopened()throws Throwable{
		session().put(getClass().getName(),co);
//		v.x_l(null,null);
//		v.x_c(null,null);
		co.x_r(null,null);
	}
	protected void onmessage(final ByteBuffer bb) throws Throwable {
		final byte cmd=bb.get();
		if(cmd==0){//key
			final byte key=bb.get();
			co.ra.set(0x0080*0x0100-1,key);
		}else if(cmd==49){//compile
//			System.out.println(cmd);
			final String src=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
//			System.out.println(src);
			co.ec.src.set(src);
			try{
//				v.x_c(null,null);
			}catch(final Throwable t){
				final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1"+b.b.stacktrace(t)));
				endpoint_recv(bbe,false);
				return;
			}
			final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1"+co.st.toString()));
			endpoint_recv(bbe,false);
			co.x_r(null,null);
		}
//		System.out.println(bb.remaining()+"  "+key);
		co.x_f(null,null);
//		final long t0=System.currentTimeMillis();
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(32*1024);
		co.snapshot_png_to(baos);
		final ByteBuffer[]bbpng=new ByteBuffer[]{ByteBuffer.wrap(new byte[]{0}),ByteBuffer.wrap(baos.toByteArray())};
//		final long t1=System.currentTimeMillis();
		while(issending()){
			System.out.println(new Date()+"\t"+session().id()+"\tstaling");
			try{Thread.sleep(20);}catch(final InterruptedException ignored){}
		}
		endpoint_recv(bbpng,false);
//		final long t2=System.currentTimeMillis();
	}
}
