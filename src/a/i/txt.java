package a.i;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import b.a;
import b.b;
import b.cacheable;
import b.req;
import b.xwriter;
public final class txt extends a implements cacheable{
	static final long serialVersionUID=1;
	public boolean cacheforeachuser(){return false;}
	public String lastmod(){return b.tolastmodstr(0);}
	public long lastmodupdms(){return 24*60*60*1000;}
	public String filetype(){return "png";}
	public String contenttype(){return "image/"+filetype();}
	public void to(final xwriter x) throws Throwable{
		final String[]s=req.get().query().split("~");
		final String font_name=s[0];
		final float font_size=Float.parseFloat(s[1]);
		final int bgcolor=Integer.parseInt(s[2],16);
		final int fgcolor=Integer.parseInt(s[3],16);
		final String txt=b.urldecode(s[4]);
		
		final Font font=$.fontget(font_name,font_size);
		final Rectangle2D r=strbounds(font,txt);
		final BufferedImage bi=new BufferedImage((int)r.getWidth(),(int)r.getHeight(),BufferedImage.TYPE_INT_RGB);
		final Graphics2D g=bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(bgcolor));
		g.fillRect(0,0,bi.getWidth(),bi.getHeight());
		g.setFont(font);
		g.setColor(new Color(fgcolor));
		g.drawString(txt,0,(int)-r.getY());
		ImageIO.write(bi,filetype(),x.outputstream());
	}
	public static Rectangle2D strbounds(final Font font,final String str){
		final Graphics2D g=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).createGraphics();
		final FontMetrics fm=g.getFontMetrics(font);
		final Rectangle2D r=fm.getStringBounds(str,g);
		return r;		
	}
	public static void tag(final xwriter x,final String str,final String ttf,final float size,final int bgcolor,final int fgcolor){
		x.p("<img src=/i/txt?").p(ttf).p("~").p(size).p("~").p(Integer.toString(fgcolor,16)).p("~").p(Integer.toString(bgcolor,16)).p("~").p(b.urlencode(str)).p(">");
	}
}
