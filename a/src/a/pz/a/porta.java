package a.pz.a;
import static b.b.K;
import static b.b.stacktraceline;
import static b.b.strenc;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import a.pz.core;
import a.pz.prog;
import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	private core co;
	final protected void onopened()throws Throwable{
		co=new core(16,8,8,32*K,1*K);
	}
	public static String tostr(final ByteBuffer bb){try{return new String(bb.array(),bb.position(),bb.remaining(),strenc);}catch(Throwable t){throw new Error(t);}}
	protected void onmessage(final ByteBuffer bb) throws Throwable{
		switch(bb.get()){
		case'0'://keys
			final byte key=bb.get();
			co.ram[0x7fff]=key;
	//		final long t0=System.nanoTime();
			break;
		case'1'://compile
			final String src=tostr(bb);
			try{
				new prog(src).zap(co.rom);
				co.reset();
				send_binary("1");
			}catch(final Throwable t){
				send_binary("1",t.toString());
			}
			break;
		default:throw new Error();
		}
		try{while(co.instruction!=-1)
				co.step();
		}catch(Throwable t){
			send_binary("2",stacktraceline(t));
			return;
		}
//		final long t1=System.nanoTime();
//		b.b.pl("zn step micro dt: "+(t1-t0)/1000);
		final int wi=256,hi=128;
		final byte[]png=png_from_bitmap(co.ram,wi,hi);
//		final long t2=System.nanoTime();
//		b.b.pl("snapshot micro dt: "+(t2-t1)/1000);
//			while(issending()){//?
//				pl(new Date()+"\t"+session().id()+"\tstaling");
//				final long stall_ms=20;
//				try{Thread.sleep(stall_ms);}catch(final InterruptedException ignored){}
//			}
		send_binary(refresh_display,png);
//		final long t3=System.nanoTime();
//		b.b.pl("send micro dt: "+(t3-t2)/1000);
	}
	private static final byte[]refresh_display=new byte[]{'0'};

	public static byte[]png_from_bitmap(final int[]bmp,final int wi,final int hi)throws IOException{
		final int bytes_per_pixel=2;
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(wi*hi*bytes_per_pixel);
		bitmap_as_png_to_outputstream(bmp,wi,hi,baos);
		return baos.toByteArray();
	}
	public static void bitmap_as_png_to_outputstream(final int[]bmp,final int wi,final int hi,final OutputStream os)throws IOException{
		final BufferedImage bi=new BufferedImage(wi,hi,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<hi;i++){
			for(int j=0;j<wi;j++){
				final int d=bmp[k++];
				final int b= (d     &0xf)*0xf;
				final int g=((d>> 4)&0xf)*0xf;
				final int r=((d>> 8)&0xf)*0xf;
//				final int a=((d>>12)&0xf)*0xf;
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
