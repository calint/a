package a.tinitus;

import b.a;
import b.cacheable;
import b.xwriter;

public class $ extends a implements cacheable{
	@Override public boolean cacheforeachuser() {return false;}
	@Override public String contenttype() {return null;}
	@Override public String filetype() {return null;}
	@Override public String lastmod() {return null;}
	@Override public long lastmodupdms() {return 0;}
	@Override public void to(xwriter x) throws Throwable {
		b.b.cp(getClass().getResourceAsStream("index.html"),x.outputstream());
	}
}
