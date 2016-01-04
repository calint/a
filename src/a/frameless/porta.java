package a.frameless;

import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;

public class porta extends websock implements threadedsock {
	@Override
	protected void onopened() throws Throwable {
		final ByteBuffer bb = ByteBuffer.allocate(8);
		byte i=1;
		while (true) {
			bb.put((byte) i++);
			bb.flip();
			send_binary(bb);
			bb.flip();
			Thread.sleep(1000);
		}
	}

	@Override
	protected void onmessage(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub
		super.onmessage(bb);
	}

	@Override
	public void onconnectionlost() throws Throwable {
		// TODO Auto-generated method stub
		super.onconnectionlost();
	}

	@Override
	protected void onclosed() throws Throwable {
		// TODO Auto-generated method stub
		super.onclosed();
	}

}
