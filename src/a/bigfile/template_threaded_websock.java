package a.bigfile;

import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;

public class template_threaded_websock extends websock implements threadedsock{
	@Override protected void onopened()throws Throwable{}
	@Override protected void onmessage(ByteBuffer bb)throws Throwable{}
	@Override public void onconnectionlost()throws Throwable{}
	@Override protected void onclosed()throws Throwable{}
	private static final long serialVersionUID=1;
}
