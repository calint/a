package a.frameless;

import java.nio.ByteBuffer;

import b.threadedsock;
import b.websock;

public class portb extends websock implements threadedsock {
	@Override
	protected void onopened() throws Throwable {
		final ByteBuffer bb = ByteBuffer.allocate(1024*8);
		int i=65;
		int x=10,y=10;
		while (true) {
//			bb.put(("document.writeln('<div style=\\'border:1px dotted blue\\' id=_"+i+">div "+i+"</div>');console.log('"+(byte) i+++"');\n").getBytes());
//			bb.put(("$t('hello "+(byte) i+++"');\n").getBytes());
			bb.clear();
			bb.put(("$w('-"+i+"',"+x+","+y+",'hello "+i+++"');\n").getBytes());
			bb.flip();
			x+=20;
			if(x>1000){x=10;y+=20;}
			send(bb,true);
//			Thread.sleep(1000);
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
