package a.pcmega;
import java.nio.*;

import b.websock;
final public class i extends websock{static final long serialVersionUID=1;
	private int n;
	synchronized final protected void onmessage(final ByteBuffer bb)throws Throwable{
		final vintage v=(vintage)session().get(o.class.getName());
		if(v==null){
			System.out.println("vintage not in session yet");
			return;
		}
		final byte b=bb.get();
		v.ram.set(2,b*b);
		n++;
	}
	protected void onclosed()throws Throwable{
		final vintage v=(vintage)session().get(o.class.getName());
		if(v==null)return;
		v.x_stop(null,null);
		session().remove(getClass().getName());
	}
}
