package a.pczero;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import b.threadedsock;
import b.websock;
final public class io extends websock implements threadedsock{static final long serialVersionUID=1;
	private final static Collection<websock>socks=new LinkedList<websock>();
	final protected void onopened()throws Throwable{
//		final ByteBuffer bbs=ByteBuffer.wrap((".e "+session().id()).getBytes());
//		for(final websock ws:socks){
//			ws.endpoint_recv(bbs.slice());
//		}
		synchronized(socks){socks.add(this);}
//		while(true){
//			final long t0=System.currentTimeMillis();
//			final Rectangle dim=new Rectangle(256,128);
//			final BufferedImage bi=new Robot().createScreenCapture(dim);
//			final ByteArrayOutputStream baos=new ByteArrayOutputStream();
//			ImageIO.write(bi,"png",baos);
//			final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
//			final long t1=System.currentTimeMillis();
//		//	System.out.println(session().id()+"   "+(t1-t0)+" ms   "+bbpng.limit());
//			for(final websock ws:socks){
//				//if(ws.isrecv()){System.out.println("busy");continue;}
//				ws.endpoint_recv(bbpng,false);
////				final ByteBuffer[]bba=new ByteBuffer[]{bb.slice()};
////				ws.endpoint_recv(bba,false);
//			}
//			n++;
//		}
	}
	private int n;
	final protected void onmessage(final ByteBuffer bb)throws Throwable{
		System.out.println(bb.remaining()+"   "+bb.get());
		final long t0=System.currentTimeMillis();
		final Rectangle dim=new Rectangle(256,128);
		final BufferedImage bi=new Robot().createScreenCapture(dim);
		final ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(bi,"png",baos);
		final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
		final long t1=System.currentTimeMillis();
	//	System.out.println(session().id()+"   "+(t1-t0)+" ms   "+bbpng.limit());
		for(final websock ws:socks){
			//if(ws.isrecv()){System.out.println("busy");continue;}
			ws.endpoint_recv(bbpng,false);
//				final ByteBuffer[]bba=new ByteBuffer[]{bb.slice()};
//				ws.endpoint_recv(bba,false);
		}
		n++;
	}
	final protected void onclosed()throws Throwable{
		synchronized(socks){socks.remove(this);}
//		final ByteBuffer bbs=ByteBuffer.wrap((".x "+session().id()).getBytes());
//		for(final websock ws:socks){
//			ws.endpoint_recv(bbs.slice());
//		}
	}
}
