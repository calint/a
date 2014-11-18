package a.pz;
import java.nio.*;

import b.threadedsock;
import b.websock;
final public class portb extends websock implements threadedsock{static final long serialVersionUID=1;
	synchronized final protected void onmessage(final ByteBuffer bb)throws Throwable{
		final vintage v=(vintage)session().get(porta.class.getName());
		if(v==null){
			System.out.println("vintage not in session yet");
			return;
		}
		final String src=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
		System.out.println(src);
		v.src.txt.set(src);
		try{
			v.x_c(null,null);
		}catch(final Throwable t){
			final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes(b.b.stacktrace(t)));
			endpoint_recv(bbe);
			return;
		}
		final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes(v.sts.toString()));
		endpoint_recv(bbe);
		v.x_r(null,null);
	}
}
