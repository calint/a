package a.i;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import b.a;
import b.b;
import b.cacheable;
import b.osnl;
import b.path;
import b.req;
import b.xwriter;
public class log extends a implements cacheable{
	static final long serialVersionUID=1;
	public static String imgtype="png";
	private path path=req.get().session().path("log.txt");
	public boolean cacheforeachuser(){return true;}
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
		final Graphics2D g=(Graphics2D)bi.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,bi.getWidth(),bi.getHeight());
		g.setColor(Color.black);
		final Font font=$.fontget("slkscr",7);
		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		final dot dot=new dot();
		dot.x=1;
		dot.y=g.getFontMetrics().getAscent();;
		dot.dy=g.getFontMetrics().getHeight();
		final String s=b.tolastmodstr(path.lastmod());
		g.drawString(s,dot.x,dot.y);
		dot.nl();
		if(path.exists()){
			path.to(new osnl(){
				public void onnewline(String line) throws Throwable{
					g.drawString(line,dot.x,dot.y);
					dot.nl();
			}});
		}
		ImageIO.write(bi,imgtype,x.outputstream());
	}
}
