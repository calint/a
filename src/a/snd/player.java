package a.snd;
import static b.b.pl;
import b.a;
import b.cacheable;
import b.xwriter;
final public class player extends a implements cacheable{static final long serialVersionUID=1;
public boolean cacheforeachuser(){return false;}
@Override public String filetype(){return"html";}
	@Override public String contenttype(){return"text/html";}
	@Override public String lastmod(){return null;}
	@Override public long lastmodupdms(){return 1000;}

	@Override public void to(final xwriter x)throws Throwable{
		pl("&& player");
		x.pl("<audio controls preload=meta seekable style=width:100%><source src=/"+au.class.getName().substring(b.b.webobjpkg.length())+" type=audio/basic></audio>");
	}
}