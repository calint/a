package a.y;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;

import b.a;
import b.sock;
import b.sockio;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;//?
public class websock extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	private final ByteBuffer bbi=ByteBuffer.allocate(b.b.K);
	private int counter;
	final public op sockinit(final Map<String,String>hdrs,sockio so)throws Throwable{
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
		final ByteBuffer bbo=ByteBuffer.allocate(b.b.K>>2);
		bbo.put(("HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: "+replkey+"\r\n\r\n").getBytes());
		bbo.flip();
		so.write(bbo);
		if(bbo.hasRemaining())throw new Error("packetnotfullysent");
		return op.read;
	}
	final public op write()throws Throwable{throw new IllegalStateException();}
	final public op read()throws Throwable{
		//? if bb.hasremaining
		bbi.clear();
		so.read(bbi);
		bbi.flip();
		while(true){
			// rfc6455#section-5.2
			// Base Framing Protocol
			final int b0=(int)bbi.get();
			final boolean fin=(b0&1)==1;
			final int resv=(b0>>1)&7;
			if(resv!=0)throw new Error("websock reserved bits are not 0");
			final int opcode=(b0>>4)&0xf;// seems to always b close
			
			final int b1=(int)bbi.get();
			final boolean mask=(b1&1)==1;// always masked
//			if(!mask)throw new Error("websock client sending unmasked message");
			long payloadlen=b1&127;
			if(payloadlen==126){// not tried
				payloadlen=(long)bbi.getShort();//? unsigned short
			}else if(payloadlen==127){// not tried
				payloadlen=bbi.getLong();
			}
			
			final byte maskkey[]=new byte[4];
			bbi.get(maskkey,0,maskkey.length);
	
			final demask dm=new demask(bbi,maskkey);
			final StringBuilder sb=new StringBuilder();
			long n=payloadlen;if(n<1)throw new Error();
			while(n--!=0){
				sb.append((char)dm.nextbyte());
			}
			
			final String reply=reply(sb.toString());
			final byte[]data=b.b.tobytes(reply);
			final int ndata=data.length;
			// build frame
			int nhdr=0;
			byte[]hdr=new byte[10];
			hdr[0]=(byte)129;
			if(ndata<=125){
				hdr[1]=(byte)ndata;
				nhdr=2;
			}else if(ndata>=126&&ndata<=65535){
				hdr[1]=(byte)126;
				byte len=(byte)ndata;
				hdr[2]=(byte)((len>>8)&(byte)255);
				hdr[3]=(byte)(len&(byte)255); 
				nhdr=4;
			}else{
				hdr[1]=(byte)127;
				byte len=(byte)ndata;
				hdr[2]=(byte)((len>>56)&(byte)255);
				hdr[3]=(byte)((len>>48)&(byte)255);
				hdr[4]=(byte)((len>>40)&(byte)255);
				hdr[5]=(byte)((len>>32)&(byte)255);
				hdr[6]=(byte)((len>>24)&(byte)255);
				hdr[7]=(byte)((len>>16)&(byte)255);
				hdr[8]=(byte)((len>>8)&(byte)255);
				hdr[9]=(byte)(len&(byte)255);
				nhdr=10;
			}
			final int nframe=nhdr+ndata;
			byte[]frame=new byte[nframe];
			int n1=0;
			for(int i=0;i<nhdr;i++){
				frame[n1]=hdr[i];
				n1++;
			}
			for(int i=0;i<ndata;i++){
				frame[n1]=data[i];
				n1++;
			}
			final ByteBuffer bbo=ByteBuffer.wrap(frame,0,n1);
			so.write(bbo);
			if(bbo.hasRemaining())throw new Error("packet not fully sent "+bbo.limit());
			if(bbi.hasRemaining())continue;//? waitif op.write
			return op.read;
		}
	}
	protected String reply(final String req)throws Throwable{
		return counter+++" "+req+" "+new Date();
//		System.out.println(new String(msg));
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
}
