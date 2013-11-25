package a;
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
	private static enum state{handshake,parse_next_frame,continue_read_payload,write,writecont,closed};
	private state st=state.closed;
	final public op sockinit(final Map<String,String>hdrs,final sockio so)throws Throwable{
		this.so=so;
		st=state.handshake;
		// rfc6455#section-1.3
		// Opening Handshake
//		if(!"13".equals(hdrs.get("sec-websocket-version")))throw new Error("sec-websocket-version not 13");
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
		bbo.put(("HTTP/1.1 101\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: "+replkey+"\r\n\r\n").getBytes());
		bbo.flip();
		so.write(bbo);
		if(bbo.hasRemaining())throw new Error("packetnotfullysent");
		
		bbi.position(bbi.limit());//enterloopreadyforread
		st=state.parse_next_frame;
		return op.read;
	}
	final public op read()throws Throwable{
		if(!bbi.hasRemaining()){
			bbi.clear();
			so.read(bbi);
			bbi.flip();
		}
		while(true){
			switch(dobbi()){default:throw new Error();
			case read:if(bbi.hasRemaining())continue;return op.read;
			case write:return op.write;
			}
		}
	}
	private final byte[]maskkey=new byte[4];
	private int payloadlen;//? long
	private byte[]data;
	private op dobbi()throws Throwable{
		switch(st){
		case parse_next_frame:
			// rfc6455#section-5.2
			// Base Framing Protocol
			final int b0=(int)bbi.get();
			final boolean fin=(b0&128)==128;
			final int resv=(b0>>4)&7;
			if(resv!=0)throw new Error("websock reserved bits are not 0");
			final int opcode=b0&0xf;
			final int b1=(int)bbi.get();
			final boolean masked=(b1&128)==128;
			if(!masked)throw new Error("websock client sending unmasked message");
			payloadlen=b1&127;
			if(payloadlen==126){
				payloadlen=(((int)bbi.get())<<8)+bbi.get();
			}else if(payloadlen==127){// not tried
				throw new Error("payloadlen==127");
			}
			if(opcode==8&&payloadlen==0){return op.close;};//?. sendcloseframe
			bbi.get(maskkey);
			st=state.continue_read_payload;
		case continue_read_payload:
			{//demask
			final byte[]bbia=bbi.array();
			final int lim=bbi.limit();
			final int pos=bbi.position();
			final int n=bbi.remaining()>payloadlen?pos+payloadlen:lim;
			int c=0;
			for(int i=pos;i<n;i++){
				final byte b=(byte)(bbia[i]^maskkey[c]);
				bbia[i]=b;
				c++;
				c%=maskkey.length;
			}}
			break;
		default:throw new Error();
		}
		switch(parse(bbi)){	default:throw new Error();
		case 2://more read
			return op.read;
		case 1://send frame
			final int ndata=data.length;
			// reply frame
			int nhdr=0;
			final byte[]hdr=new byte[10];
			hdr[0]=(byte)129;
			if(ndata<=125){
				hdr[1]=(byte)ndata;
				nhdr=2;
			}else if(ndata>=126&&ndata<=65535){
				hdr[1]=126;
				hdr[2]=(byte)((ndata>>8)&255);
				hdr[3]=(byte)( ndata    &255);
				nhdr=4;
			}else{
				hdr[1]=127;
	//			hdr[2]=(byte)((ndata>>56)&255);
	//			hdr[3]=(byte)((ndata>>48)&255);
	//			final long la=ndata>>40;
	//			final long laa=la&255;
	//			hdr[4]=(byte)((ndata>>40)&255);
	//			hdr[5]=(byte)((ndata>>32)&255);
				hdr[6]=(byte)((ndata>>24)&255);
				hdr[7]=(byte)((ndata>>16)&255);
				hdr[8]=(byte)((ndata>> 8)&255);
				hdr[9]=(byte)( ndata     &255);
				nhdr=10;
			}
			bbos=new ByteBuffer[]{ByteBuffer.wrap(hdr,0,nhdr),ByteBuffer.wrap(data)};
			st=state.parse_next_frame;//state after write done
			return write();
		}
	}
	private ByteBuffer[]bbos;
	final public op write()throws Throwable{
		so.write(bbos);
		for(final ByteBuffer b:bbos)if(b.hasRemaining())return op.write;
//		if(bbi.hasRemaining())return dobbi();
		return op.read;
	}
	protected int parse(final ByteBuffer bb)throws Throwable{
		final String msg=new String(bb.array(),bb.position(),payloadlen);
		bb.position(bb.position()+payloadlen);
		System.out.println(msg);
		final StringBuilder sb=new StringBuilder();
//		for(int i=0;i<200000;i++)sb.append((char)b.b.rndint('a','z'));
		data=(counter+++" "+msg+" "+new Date()+" "+sb).getBytes();
		return 1;
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
