package a.qa;
import b.a;
import b.cacheable;
import b.xwriter;
final public class t011 extends a implements cacheable{static final long serialVersionUID=1;
	public String filetype(){return "html";}
	public String contenttype(){return "html/text";}
	public String lastmod(){return null;}
	public long lastmodupdms(){return 24*60*60*1000;}
	public boolean cacheforeachuser(){return false;}

	public void to(final xwriter x)throws Throwable{}
}