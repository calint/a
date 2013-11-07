package a.y;
import java.awt.GraphicsEnvironment;
import b.a;
import b.xwriter;
public class fonts extends a{
	static final long serialVersionUID=1;
//	private static String lastmodstr;
//	@Override public boolean cacheforeachuser(){return false;}
//	@Override public String contentType(){return "text/plain";}
//	public String lastMod(){return lastmodstr;};
//	@Override public long lastModChk_ms(){return 10000;}
	@Override public void to(xwriter x) throws Throwable{
//		lastmodstr=htp.toLastModified(System.currentTimeMillis());
		GraphicsEnvironment env=GraphicsEnvironment.getLocalGraphicsEnvironment();
		x.pre();
		for(String f:env.getAvailableFontFamilyNames()){
			x.pl(f);
		}
	}
}
