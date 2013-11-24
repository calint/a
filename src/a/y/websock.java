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
		// rfc6455#section-1.3
		// Opening Handshake
		if(!"13".equals(hdrs.get("sec-websocket-version")))throw new Error("sec-websocket-version not 13");
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
//		hs.put(("HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: "+replkey+"\r\nSec-WebSocket-Protocol: chat\r\n\r\n").getBytes());
		hs.put(("HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: "+replkey+"\r\n\r\n").getBytes());
		hs.flip();
		return op.write;
	}
	public op read()throws Throwable{
		so.read(bb);
		bb.flip();
		// rfc6455#section-5.2
		// Base Framing Protocol
		final int b0=(int)bb.get();
		final boolean fin=(b0&1)==1;
		final int resv=(b0>>1)&7;
		if(resv!=0)throw new Error("websockprot resv!=0");
		final int opcode=(b0>>4)&0xf;
		
		final int b1=(int)bb.get();
		final boolean mask=(b1&1)==1;
		long payloadlen=b1&127;
		if(payloadlen==126){
			payloadlen=(long)bb.getShort();//? unsigned short
		}else if(payloadlen==127){
			payloadlen=bb.getLong();
		}
		
		byte maskkey[]=new byte[4];
		maskkey[0]=bb.get();
		maskkey[1]=bb.get();
		maskkey[2]=bb.get();
		maskkey[3]=bb.get();
		
		if(opcode==8){//close
			process(bb,maskkey,false);
			return op.close;
		}
		return op.read;
	}
	private final static class demask{
		private final ByteBuffer bb;
		private final byte[]maskkey;
		private int c;
		public demask(final ByteBuffer bb,final byte[]maskkey){this.bb=bb;this.maskkey=maskkey;}
		public int nextbyte(){
			if(bb.remaining()==0)return -1;
			final int b=bb.get()^maskkey[c];
			c++;
			c%=maskkey.length;
			return b;
		}
	}
	private StringBuilder sb=new StringBuilder();
	private op process(final ByteBuffer bb,final byte[]maskkey,boolean async)throws Throwable{
		final demask dm=new demask(bb,maskkey);
		sb.setLength(0);
		int b;
		while((b=dm.nextbyte())!=-1){
			sb.append((char)b);
//			System.out.println((char)b);
		}
//		System.out.println();
		System.out.println(sb.toString());
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
