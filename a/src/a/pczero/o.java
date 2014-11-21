package a.pczero;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import b.threadedsock;
import b.websock;
final public class o extends websock implements threadedsock{static final long serialVersionUID=1;
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
			while(issending()){
				System.out.println(new Date()+"\t"+session().id()+"\tstaling");
				try{Thread.sleep(20);}catch(final InterruptedException ignored){}
			}
			send(bbpng,false);
			final long t2=System.currentTimeMillis();
		}
	}
}
