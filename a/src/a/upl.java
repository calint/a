package a;
import b.*;
public class upl extends a implements cacheable{static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		b.cp(getClass().getResourceAsStream("upload.html"),x.outputstream());
	}
	
	// cacheable
	public String filetype(){return "html";}
	public String contenttype(){return "text/html;charset=utf8";}
	public String lastmod(){return lastmod;}
	public long lastmodupdms(){return 1000*60;}
	public boolean cacheforeachuser(){return false;}

	private final static String lastmod=b.tolastmodstr(b.timeatload);
}