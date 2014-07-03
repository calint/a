package a.pczero;
import static b.b.pl;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Base64;
import javax.imageio.ImageIO;
import b.a;
import b.xwriter;
final public class ram extends a{
	static final long serialVersionUID=1;
	public final static int width=256;
	public final static int height=128;	
	final int scl=2;
	public final static int size=width*height;
	private short[]ram=new short[size];
	public ram(){rst();}
	static String labelrefresh="*";
	public void rst(){x=null;for(int i=0;i<ram.length;i++)ram[i]=0;}
	public void to(final xwriter x)throws Throwable{
		x.style(pngbase64,"display:none").output(pngbase64);
		x.p("<canvas id=").p(id()).p(" width=").p(width*scl).p(" height=").p(height*scl).p("></canvas>");
	}
	public a pngbase64;
	public void x_rfh(final xwriter x,final String s)throws Throwable{
		final int size_of_short_in_bytes=2;
//		final ByteBuffer bb=ByteBuffer.allocate(ram.length*size_of_short_in_bytes);
//		bb.asShortBuffer().put(ram);
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(ram.length*size_of_short_in_bytes);
		final BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		int k=0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				final short d=get(k++);
				final int b= (d    &0xf)*0xf;
				final int g=((d>>4)&0xf)*0xf;
				final int r=((d>>8)&0xf)*0xf;
//				final int a=(d>>12)&0xf;
				final int a=0xff;
				final int argb=(a<<24)+(r<<16)+(g<<8)+b;
				bi.setRGB(j,i,argb);
			}
		}
		ImageIO.write(bi,"png",baos);
		
//		snapshot(baos);
		final ByteBuffer bbpng=ByteBuffer.wrap(baos.toByteArray());
		final ByteBuffer png_base64=Base64.getEncoder().encode(bbpng);
		final String png_base64_str=new String(png_base64.array(),png_base64.position(),png_base64.limit());
		pngbase64.set(png_base64_str);
		x.xu(pngbase64);
		x.pl("var c=$('"+id()+"');var d=c.getContext('2d');var i=new Image;i.onload=function(){d.drawImage(i,0,0,c.width,c.height);};i.src='data:image/png;base64,'+$('"+pngbase64.id()+"').value;");
//		ev(x,this,"newframe");
		
		
//		final String id=id();
//		x.p("var d2=$('").p(id).p("').getContext('2d');");
//		int cell=0;
//		final int yw=height;//? size>>12;
//		final int xw=width;
//		for(int y=0;y<yw;y++){
//			for(int xx=0;xx<xw;xx++){
//				final short argb=ram[cell++];
//				final String hex=Integer.toHexString(argb);
//				x.p("d2.fillStyle='#"+vintage.fld("000",hex)+"';");
//				x.pl("d2.fillRect("+xx*scl+","+y*scl+","+scl+","+scl+");");
//			}
//		}
	}
	public short get(final int addr){
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
		ram[a]=(short)value;
		if(x==null)return;
		final short argb=(short)value;
		final String hex=Integer.toHexString(argb);
		final String id=id();
		x.p("{var d2=$('").p(id).p("').getContext('2d');");
		x.p("d2.fillStyle='#"+vintage.fld("000",hex)+"';");
		final int yy=a/width;
		final int xx=a%width;
		final int scl=2;
		x.p("d2.fillRect("+xx*scl+","+yy*scl+","+scl+","+scl+");");				
		x.pl("}");
	}
}
