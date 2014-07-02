package a.i;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.imageio.ImageIO;
import b.a;
import b.b;
import b.cacheable;
import b.osnl;
import b.path;
import b.xwriter;
public final class src extends a implements cacheable{
	static final long serialVersionUID=1;
	private static final BufferedImage bi=new BufferedImage(512,512,BufferedImage.TYPE_INT_RGB);
	private static final Graphics2D g=bi.createGraphics();
	private static final int[]bmp=((DataBufferInt)bi.getRaster().getDataBuffer()).getData();
	public boolean cacheforeachuser(){return false;}
	public String lastmod(){return null;}
	public long lastmodupdms(){return 10*1000;}
	public String filetype(){return "png";}
	public String contenttype(){return "image/"+filetype();}
	public void to(final xwriter x) throws Throwable{
		for(int n=0;n<bmp.length;n++)
			bmp[n]=b.rndint(0,0x000100);
		final Font font=$.fontget("slkscr",7);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		g.setFont(font);
		final String[]files=b.path("src/b").list();
		final String file=files[b.rndint(0,files.length)];
		final path filepth=b.path("src/b/"+file);
		final class dot{
			int x,y,dy;
			void nl(){y+=dy;}
		}
		final dot dot=new dot();
		dot.x=font.getSize()*2;
		dot.y=g.getFontMetrics().getAscent();
		dot.dy=font.getSize();
		final String sl=b.tolastmodstr(filepth.lastmod());
		final String sr=filepth.size()+"B";
		final String sc=filepth.toString();
		int xp=dot.x;
		dot.x=0;
		g.drawString(sl,dot.x,dot.y);
		dot.x=bi.getWidth()/2-g.getFontMetrics().stringWidth(sc)/2;
		g.drawString(sc,dot.x,dot.y);
		dot.x=bi.getWidth()-g.getFontMetrics().stringWidth(sr);
		g.drawString(sr,dot.x,dot.y);
		dot.x=xp;
		g.drawLine(0,dot.y,bi.getWidth(),dot.y);
		dot.nl();
		filepth.to(new osnl(){
			public void onnewline(final String line) throws Throwable{
				g.drawString(line.replaceAll("\\t","   "),dot.x,dot.y);
				dot.nl();
		}});
		ImageIO.write(bi,filetype(),x.outputstream());
	}
}
