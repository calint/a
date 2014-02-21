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
	}
	private int n;
	final protected void onmessage(final ByteBuffer bb)throws Throwable{
		final long t0=System.currentTimeMillis();
		final BufferedImage bi=new Robot().createScreenCapture(new Rectangle(256,128));
		final ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(bi,"png",baos);
		final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
		final long t1=System.currentTimeMillis();
		System.out.println(t1-t0+"   "+bbpng.limit());
		for(final websock ws:socks){
			try{ws.endpoint_recv(bbpng,false);}catch(final Throwable t){
				t.printStackTrace();
			}
//			final ByteBuffer[]bba=new ByteBuffer[]{bb.slice()};
//			ws.endpoint_recv(bba,false);
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
