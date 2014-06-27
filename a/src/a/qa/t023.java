package a.qa;
import b.*;
// cacheable uri
final public class t023 extends a implements cacheable{static final long serialVersionUID=1;
	public String filetype(){return "txt";}
	public String contenttype(){return "plain/text";}
	public String lastmod(){return null;}
	public long lastmodupdms(){return 24*60*60*1000;}
	public boolean cacheforeachuser(){return false;}

	public void to(final xwriter x)throws Throwable{
		b.path("qa/t001.txt").to(x.outputstream());
	}
}