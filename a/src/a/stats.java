package a;
import b.a;
import b.b;
import b.cacheable;
import b.xwriter;
public class stats extends a implements cacheable{
	static final long serialVersionUID=1;
	public String filetype(){return "txt";}
	public String contenttype(){return "text/plain;charset=utf8";}
	public boolean cacheforeachuser(){return false;}
	public String lastmod(){return null;}
	public long lastmodupdms(){return 1000;}
	public void to(final xwriter x)throws Throwable{b.stats_to(x.outputstream());}
}
