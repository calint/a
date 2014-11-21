package a.qa;
import b.*;
import java.nio.*;
final public class echothd extends websock implements threadedsock{static final long serialVersionUID=1;
	final protected void onmessage(final ByteBuffer bb)throws Throwable{
		Thread.sleep(10000);
		send(bb);
	}
}
