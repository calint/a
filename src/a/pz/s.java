package a.pz;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;
final public class s extends websock implements threadedsock{static final long serialVersionUID=1;
	final protected void onopened()throws Throwable{
		final vintage v=new vintage();
		session().put(getClass().getName(),v);
		v.x_l(null,null);
		v.x_c(null,null);
		v.x_r(null,null);
		while(true){
			v.x_f(null,null);
			if(v.stopped)
				return;
			final long t0=System.currentTimeMillis();
			final ByteArrayOutputStream baos=new ByteArrayOutputStream(32*b.b.K);
			v.snapshot(baos);
			final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
			final long t1=System.currentTimeMillis();
			endpoint_recv(bbpng,false);
			final long t2=System.currentTimeMillis();
		}
	}
}
