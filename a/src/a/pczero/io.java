package a.pczero;
import b.*;
import java.nio.*;
import java.util.*;
final public class io extends websock implements threadedsock{static final long serialVersionUID=1;
	private final static Collection<websock>socks=new LinkedList<websock>();
	final protected void onopened()throws Throwable{
//		final ByteBuffer bbs=ByteBuffer.wrap((".e "+session().id()).getBytes());
//		for(final websock ws:socks){
//			ws.endpoint_recv(bbs.slice());
//		}
		synchronized(socks){socks.add(this);}
	}
	final protected void onmessage(final ByteBuffer bb)throws Throwable{
		for(final websock ws:socks){
			final ByteBuffer[]bba=new ByteBuffer[]{bb.slice()};
			ws.endpoint_recv(bba,false);
		}
	}
	final protected void onclosed()throws Throwable{
		synchronized(socks){socks.remove(this);}
//		final ByteBuffer bbs=ByteBuffer.wrap((".x "+session().id()).getBytes());
//		for(final websock ws:socks){
//			ws.endpoint_recv(bbs.slice());
//		}
	}
}
