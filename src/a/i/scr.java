package a.i;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import b.a;
import b.cacheable;
import b.xwriter;
public class scr extends a implements cacheable{
	static final long serialVersionUID=1;
	public String filetype(){return "png";}
	public String contenttype(){return "image/png";}
	public boolean cacheforeachuser(){return false;}
	public long lastmodupdms(){return 1000;}
	public String lastmod(){return null;}
	static Robot rob;
	public static BufferedImage bi;
	public scr()throws Throwable{if(rob==null)rob=new Robot();}
	public void to(xwriter x) throws Throwable{
		bi=rob.createScreenCapture(new Rectangle(0,45,475,326));
		ImageIO.write(bi,"png",x.outputstream());
	}
}
