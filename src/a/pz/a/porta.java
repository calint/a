package a.pz.a;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import javax.imageio.ImageIO;
import a.pz.core;
import a.pz.program;
import b.threadedsock;
import b.websock;
import static b.b.*;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	private core co;
	final protected void onopened()throws Throwable{
		co=new core(16,8,8,32*K,1*K);
		co.reset();
		session().put(getClass().getName(),co);
	}
	protected void onmessage(final ByteBuffer bb) throws Throwable{
		final byte cmd=bb.get();
		if(cmd==0){//keys
			final byte key=bb.get();
			co.ram[0x7fff]=key;
		}else if(cmd==49){//compile
			final String src=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
			try{new program(src).zap(co.rom);}catch(final Throwable t){
//				final ByteBuffer bbe=ByteBuffer.wrap(tobytes("1"+stacktrace(t)));
//				endpoint_recv(bbe,false);
				send_binary("1"+stacktrace(t));
				return;
			}
			co.reset();
			send_binary("1ok");
		}
//		final long t0=System.nanoTime();
		co.step_frame();
//		final long t1=System.nanoTime();
//		b.b.pl("zn step micro dt: "+(t1-t0)/1000);
		final int wi=256,hi=128;
//		final ByteArrayOutputStream baos=new ByteArrayOutputStream(wi*hi*bp);
		final ByteBuffer png=ByteBuffer.wrap(snapshot_png(co.ram,wi,hi));
//		final long t2=System.nanoTime();
//		b.b.pl("snapshot micro dt: "+(t2-t1)/1000);
		while(issending()){//?
			pl(new Date()+"\t"+session().id()+"\tstaling");
			final long stall_ms=20;
			try{Thread.sleep(stall_ms);}catch(final InterruptedException ignored){}
		}
		final ByteBuffer b0=ByteBuffer.wrap(new byte[]{0});
		final ByteBuffer[]refresh_display=new ByteBuffer[]{b0,png};
		send(refresh_display,false);
//		final long t3=System.nanoTime();
//		b.b.pl("send micro dt: "+(t3-t2)/1000);
	}
	public static byte[]snapshot_png(final int[]bmp,final int wi,final int hi)throws IOException{
		final int bytes_per_pixel=2;
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(wi*hi*bytes_per_pixel);
		snapshot_png_to(bmp,wi,hi,baos);
		return baos.toByteArray();
	}
	public static void snapshot_png_to(final int[]data,final int wi,final int hi,final OutputStream os)throws IOException{
		final BufferedImage bi=new BufferedImage(wi,hi,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<hi;i++){
			for(int j=0;j<wi;j++){
				final int d=data[k++];
				final int b= (d    &0xf)*0xf;
				final int g=((d>>4)&0xf)*0xf;
				final int r=((d>>8)&0xf)*0xf;
//				final int a=(d>>12)&0xf;
				final int a=0xff;
				final int argb=(a<<24)+(r<<16)+(g<<8)+b;
				bi.setRGB(j,i,argb);
			}
//			k+=0;//skip
		}
		ImageIO.write(bi,"png",os);
//		
//		
//		final byte[]pixels=((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
	}
}
