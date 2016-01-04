package a.tinitus;

import static b.b.pl;

import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;

public class porta extends websock implements threadedsock{
	@Override
	protected void onopened() throws Throwable {
		pl("porta opened");
		final ByteBuffer bb = ByteBuffer.allocate(4096*16);
		while (true) {
			final byte[]ba=bb.array();
			for(int i=0;i<ba.length;i++){
				ba[i]=(byte)(Math.random()*255);
			}
			send_binary(bb);
			bb.flip();
			Thread.sleep(1000);
		}
	}

	@Override
	public void onconnectionlost() throws Throwable {
		pl("porta connection lost");
	}

	@Override
	protected void onmessage(ByteBuffer bb) throws Throwable {
		pl("porta on message: " + new String(bb.array()));
	}

	@Override
	protected void onclosed() throws Throwable {
		pl("porta on closed");
	}
}
