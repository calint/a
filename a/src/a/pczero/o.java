package a.pczero;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;
final public class o extends websock implements threadedsock{static final long serialVersionUID=1;
	final protected void onopened()throws Throwable{
		final vintage v=new vintage();
		session().put(getClass().getName(),v);
		System.out.println(v.sts);
		v.x_l(null,null);
		System.out.println(v.sts);
		v.x_c(null,null);
		System.out.println(v.sts);
		v.x_r(null,null);
		System.out.println(v.sts);
		while(true){
			v.x_f(null,null);
			System.out.println(v.sts);
			final long t0=System.currentTimeMillis();
//			final Rectangle dim=new Rectangle(256,128);
//			final BufferedImage bi=new Robot().createScreenCapture(dim);
			final ByteArrayOutputStream baos=new ByteArrayOutputStream(32*b.b.K);
//			ImageIO.write(bi,"png",baos);
			v.snapshot(baos);
			final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
			final long t1=System.currentTimeMillis();
		//	System.out.println(session().id()+"   "+(t1-t0)+" ms   "+bbpng.limit());
			endpoint_recv(bbpng,false);
		}
	}
}
