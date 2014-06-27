package a.qa;
import b.*;
import java.nio.*;
final public class echo extends websock{static final long serialVersionUID=1;
	final protected void onmessage(final ByteBuffer bb)throws Throwable{
		endpoint_recv(bb);
	}
}
