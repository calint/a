package a.i;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import b.a;
import b.b;
import b.cacheable;
import b.osnl;
import b.path;
import b.xwriter;
public class index extends a implements cacheable{
	static final long serialVersionUID=1;
	public static String imgtype="png";
	private path path=b.path("index.html");
	public boolean cacheforeachuser(){return false;}
	public String filetype(){return imgtype;}
	public String contenttype(){return "image/"+imgtype;}
	public long lastmodupdms(){return 1000;}
	public String lastmod(){return b.tolastmodstr(path.lastmod());}
	private static final class dot{
		int x,y,dy;
		void nl(){y+=dy;}
	}
	public void to(xwriter x) throws Throwable{
		final BufferedImage bi=new BufferedImage(512,512,BufferedImage.TYPE_3BYTE_BGR);
		final Graphics g=bi.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,bi.getWidth(),bi.getHeight());
		g.setColor(Color.black);
		g.setFont($.fontget("slkscr",8));
		final dot dot=new dot();
		dot.x=1;
		dot.y=11;
		dot.dy=11;
		final String s=b.tolastmodstr(path.lastmod());
		g.drawString(s,dot.x,dot.y);
		dot.nl();
		path.to(new osnl(){public void onnewline(final String ln)throws Throwable{
			g.drawString(ln,dot.x,dot.y);
			dot.nl();
		}});
		ImageIO.write(bi,imgtype,x.outputstream());
	}
}
