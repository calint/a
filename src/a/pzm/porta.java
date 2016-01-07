package a.pzm;
import static b.b.K;
import static b.b.pl;
import static b.b.stacktraceline;
import static b.b.strenc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import a.pzm.lang.reader;
import a.pzm.lang.statement;
import a.pzm.lang.xbin;
import b.req;
import b.session;
import b.threadedsock;
import b.websock;
final public class porta extends websock implements threadedsock{
	static final long serialVersionUID=1;
	private core co;
	final protected void onopened() throws Throwable{
		final session ses=req.get().session();
		final ide aide=(ide)ses.get("/pzm.ide");
		if(aide!=null){
			co=aide.cor;
			System.out.println(req.get().session().id());
		}else{
			co=new core(64,16,16,512*K,16*K);
		}
	}
	public static String tostr(final ByteBuffer bb){
		try{
			return new String(bb.array(),bb.position(),bb.remaining(),strenc);
		}catch(Throwable t){
			throw new Error(t);
		}
	}
	protected void onmessage(final ByteBuffer bb) throws Throwable{
		switch(bb.get()){
		case '0'://keys
			final byte key=bb.get();
			co.ram[0x7fff]=key;
			break;
		case '1'://compile
			final String src="{"+tostr(bb)+"}";
			try{
				//				new program(src).zap(co.rom);
				final reader r=new reader(new StringReader(src));
				final statement stmt=new statement(this, "stmt",null,"",r);
				final prog p=new prog(r.toc,stmt);
				pl("*** compile");
				for(int i=0;i<co.rom.length;i++)
					co.rom[i]=0;
				final xbin b=new xbin(p.toc,co.rom);
				p.stmt.binary_to(b);
				b.link();
				co.reset();
				pl("*** done");
				send_binary("1");
			}catch(final Throwable t){
				send_binary("1",t.toString());
			}
			break;
		default:
			throw new Error();
		}
		try{
			co.meter_instructions=0;
			boolean loop=true;
			while(true){
				co.step();
				if(loop==false)break;
				if(co.is_instruction_eof())loop=false;
			}
		}catch(Throwable t){
			send_binary("2",stacktraceline(t));
			return;
		}
		final int wi=256,hi=128;
		final byte[]png=png_from_bitmap(co.ram,wi,hi);
		send_binary("2",Long.toString(co.meter_instructions));
		send_binary(refresh_display,png);
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
				final int b=(d&0xf)*0xf;
				final int g=((d>>4 )&0xf)*0xf;
				final int r=((d>>8 )&0xf)*0xf;
//				final int a=((d>>12)&0xf)*0xf;
				final int a=0xff;
				final int argb=(a<<24)+(r<<16)+(g<<8)+b;
				bi.setRGB(j,i,argb);
			}
		}
		ImageIO.write(bi,"png",os);
	}
}
