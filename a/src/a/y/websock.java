package a.y;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Map;

import b.a;
import b.sock;
import b.sockio;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;//?
public class websock extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	private ByteBuffer bb=ByteBuffer.allocate(b.b.K);
	private ByteBuffer hs=ByteBuffer.allocate(b.b.K);
	private int state;
	public op sockinit(final Map<String,String>hdrs,sockio so)throws Throwable{
		this.so=so;
		final String key=hdrs.get("sec-websocket-key");
		final String s=key+"258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		final MessageDigest md=MessageDigest.getInstance("SHA-1");
		final byte[]sha1ed=md.digest(s.getBytes());
        final ByteArrayOutputStream baos=new ByteArrayOutputStream();
        final OutputStream encos=MimeUtility.encode(baos,"base64");
        encos.write(sha1ed);
        encos.close();
        final byte[]b64encd=baos.toByteArray();
        final String replkey=new String(b64encd);
		hs.put(("HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: "+replkey+"\r\n\r\n").getBytes());
		hs.flip();
		return op.write;
	}
	public op read()throws Throwable{
		so.read(bb);
		bb.flip();
		// decode frame
		// process (?onthread)
		// reply
//		final String s=new String(bb.array(),2,bb.limit(),"utf8");
		return op.close;
	}
	public op write()throws Throwable{
		if(state==0){
			// build frame
			so.write(hs);
			if(hs.remaining()!=0)
				return op.write;
			state=1;
		}
		return op.read;
	}

}
