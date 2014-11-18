package a.pz;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;
import javax.imageio.ImageIO;
import b.a;
import b.xwriter;
final public class ram extends a{
	static final long serialVersionUID=1;
	public final static int width=256*2;
	public final static int height=128*2;	
	public final static int size=width*height;
	final private int scl=2;
	private int[]ram=new int[size];
	public ram(){rst();}
	static String labelrefresh="*";
	public void rst(){x=null;for(int i=0;i<ram.length;i++)ram[i]=0;}
	public void to(final xwriter x)throws Throwable{
//		x.div();
		x.p("<canvas class=\"display:block\" id=").p(id()).p(" width=").p(256*scl).p(" height=").p(128*scl).p("></canvas>");
//		x.div_();
	}
	public void x_rfh(final xwriter x,final String s,final int width,final int height,final int offset,final int nl)throws Throwable{// refresh ram ui
		final int size_of_short_in_bytes=2;
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(ram.length*size_of_short_in_bytes);
		final BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				final int d=get(k++);
				final int b= (d    &0xf)*0xf;
				final int g=((d>>4)&0xf)*0xf;
				final int r=((d>>8)&0xf)*0xf;
//				final int a=(d>>12)&0xf;
				final int a=0xff;//? unused transparency
				final int argb=(a<<24)|(r<<16)|(g<<8)|b;
				bi.setRGB(j,i,argb);
			}
		}
		ImageIO.write(bi,"png",baos);
		final ByteBuffer bb_png=ByteBuffer.wrap(baos.toByteArray());
		final ByteBuffer bb_png_base64=Base64.getEncoder().encode(bb_png);
		final String str_png_base64=new String(bb_png_base64.array(),bb_png_base64.position(),bb_png_base64.limit());
		x.p("var c=$('").p(id()).p("');if(c){var d=c.getContext('2d');var i=new Image;i.onload=function(){d.drawImage(i,0,0,c.width,c.height);};i.src='data:image/png;base64,").p(str_png_base64).p("';}");
	}
	public int get(final int addr){
		final int a;
//		if(addr>=ram.length){
//			a=addr%ram.length;
//		}else
			a=addr;
		return ram[a];
	}
	xwriter x;// if set updates to ram display are written as js
	public void set(final int addr,final int value){
		final int a;
//		if(addr>=ram.length){
//			a=addr%ram.length;
//		}else
			a=addr;
		ram[a]=value;
		if(x==null)return;
		final int argb=value;
		final String hex=Integer.toHexString(argb);
		final String id=id();
		x.p("{var d2=$('").p(id).p("');if(d2){d2.getContext('2d');");
		x.p("d2.fillStyle='#"+zn.fld("000",hex)+"';");
		final int yy=a/width;
		final int xx=a%width;
		final int scl=2;
		x.p("d2.fillRect("+xx*scl+","+yy*scl+","+scl+","+scl+");");				
		x.pl("}}");
	}
}
