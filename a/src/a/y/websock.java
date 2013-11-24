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
				throw new Error("payloadlen==126");
//				payloadlen=(long)bbi.getShort();//? unsigned short
			}else if(payloadlen==127){// not tried
				throw new Error("payloadlen==126");
//				payloadlen=bbi.getLong();
			}
			
			final byte maskkey[]=new byte[4];
			bbi.get(maskkey,0,maskkey.length);
	
			//?. demask on array backing bbi
			final demask dm=new demask(bbi,maskkey);
			final byte[]req=new byte[(int)payloadlen];
			for(int i=0;i<payloadlen;i++){
				req[i]=dm.nextbyte();
			}
			
			
			
			
			
			final byte[]data=reply(req);
			final int ndata=data.length;
			
			
			
			
			
			
			
			// build frame
			int nhdr=0;
			final byte[]hdr=new byte[10];
			hdr[0]=(byte)129;
			if(ndata<=125){
				hdr[1]=(byte)ndata;
				nhdr=2;
			}else if(ndata>=126&&ndata<=65535){
				hdr[1]=126;
				hdr[2]=(byte)(ndata>>8&255);
				hdr[3]=(byte)(ndata&255); 
				nhdr=4;
			}else{
				hdr[1]=127;
				hdr[2]=(byte)(ndata>>56&255);
				hdr[3]=(byte)(ndata>>48&255);
				hdr[4]=(byte)(ndata>>40&255);
				hdr[5]=(byte)(ndata>>32&255);
				hdr[6]=(byte)(ndata>>24&255);
				hdr[7]=(byte)(ndata>>16&255);
				hdr[8]=(byte)(ndata>>8&255);
				hdr[9]=(byte)(ndata&255);
				nhdr=10;
			}
			final int nframe=nhdr+ndata;
			byte[]frame=new byte[nframe];
			int m=0;
			for(int i=0;i<nhdr;i++)frame[m++]=hdr[i];
			for(int i=0;i<ndata;i++)frame[m++]=data[i];
			final ByteBuffer bbo=ByteBuffer.wrap(frame);
			so.write(bbo);
			if(bbo.hasRemaining())
				throw new Error("packet not fully sent "+bbo.limit());
			if(bbi.hasRemaining())continue;//? waitif op.write
			return op.read;
		}
	}
	protected byte[]reply(final byte[]req)throws Throwable{
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<200;i++)sb.append("a");
		return (counter+++" "+new String(req)+" "+new Date()+" "+sb).getBytes();
//		System.out.println(new String(msg));
	}
	private final static class demask{
		private final ByteBuffer bb;
		private final byte[]maskkey;
		private int c;
		public demask(final ByteBuffer bb,final byte[]maskkey){this.bb=bb;this.maskkey=maskkey;}
		public byte nextbyte(){
			if(bb.remaining()==0)return -1;
			final byte b=(byte)(bb.get()^maskkey[c]);
			c++;
			c%=maskkey.length;
			return b;
		}
	}
}
