package a.pz.a;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.Date;
import javax.imageio.ImageIO;
import a.pz.core;
import a.pz.program;
import a.pz.source_reader;
import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{static final long serialVersionUID=1;
	private core co;
	final protected void onopened()throws Throwable{
		co=new core(16,8,8,32*1024,1*1024);
//		new program(new source_reader(new InputStreamReader(getClass().getResourceAsStream("pz.src")))).write_to(co.rom);
		co.reset();
		session().put(getClass().getName(),co);
	}
	protected void onmessage(final ByteBuffer bb) throws Throwable{
		final byte cmd=bb.get();
		if(cmd==0){//key
			final byte key=bb.get();
			co.ram[0x0080*0x0100-1]=key;
		}else if(cmd==49){//compile
//			System.out.println(cmd);
			final String src=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
//			System.out.println(src);
			try{
				new program(new source_reader(new StringReader(src))).write_to(co.rom);
			}catch(final Throwable t){
				final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1"+b.b.stacktrace(t)));
				endpoint_recv(bbe,false);
				return;
			}
			co.reset();
			final ByteBuffer bbe=ByteBuffer.wrap(b.b.tobytes("1ok"));
			endpoint_recv(bbe,false);
		}
//		System.out.println(bb.remaining()+"  "+key);
		co.step_frame();
//		final long t0=System.currentTimeMillis();
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(32*1024);
		snapshot_png_to(co.ram,256,128,baos);
		final ByteBuffer[]bbpng=new ByteBuffer[]{ByteBuffer.wrap(new byte[]{0}),ByteBuffer.wrap(baos.toByteArray())};
//		final long t1=System.currentTimeMillis();
		while(issending()){
			System.out.println(new Date()+"\t"+session().id()+"\tstaling");
			try{Thread.sleep(20);}catch(final InterruptedException ignored){}
		}
		endpoint_recv(bbpng,false);
//		final long t2=System.currentTimeMillis();
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
