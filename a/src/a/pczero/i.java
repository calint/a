package a.pczero;
import java.nio.*;
import b.websock;
final public class i extends websock{static final long serialVersionUID=1;
	private int n;
	synchronized final protected void onmessage(final ByteBuffer bb)throws Throwable{
//		System.out.println(bb.remaining()+"   "+bb.get());
		n++;
	}
}
