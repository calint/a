package a.pczero;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import b.threadedsock;
import b.websock;
final public class io extends websock implements threadedsock{static final long serialVersionUID=1;
	final protected void onopened()throws Throwable{
		enableclientinput();
		while(true){
			sendscreen();
//			Thread.sleep(10000);
		}
	}
	private int n;
	final protected void onmessage(final ByteBuffer bb)throws Throwable{
		System.out.println(bb.remaining()+"   "+bb.get());
		n++;
	}
	final protected void onclosed()throws Throwable{}
	private void sendscreen() throws AWTException, IOException, Throwable {
		final long t0=System.currentTimeMillis();
		final Rectangle dim=new Rectangle(256,128);
		final BufferedImage bi=new Robot().createScreenCapture(dim);
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(32*b.b.K);
		ImageIO.write(bi,"png",baos);
		final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
	//	System.out.println(session().id()+"   "+(t1-t0)+" ms   "+bbpng.limit());
		endpoint_recv(bbpng,false);
		final long t1=System.currentTimeMillis();
	}
}
