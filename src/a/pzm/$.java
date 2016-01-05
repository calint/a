package a.pzm;
import b.a;
import b.b;
import b.cacheable;
import b.xwriter;
// cacheable uri
final public class $ extends a implements cacheable{static final long serialVersionUID=1;
	public String filetype(){return "html";}
	public String contenttype(){return "text/html;charset=utf8";}
	public String lastmod(){return null;}
	public long lastmodupdms(){return 0;}
//	public long lastmodupdms(){return 24*60*60*1000;}
	public boolean cacheforeachuser(){return false;}

	public void to(final xwriter x)throws Throwable{
		b.cp(getClass().getResourceAsStream("index.html"),x.outputstream());
	}
}