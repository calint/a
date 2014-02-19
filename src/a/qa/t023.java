package a.qa;
import b.a;
import b.b;
import b.path;
import b.xwriter;

import java.nio.ByteBuffer;
import b.cacheable;
// cacheable uri
final public class t023 extends a implements cacheable{static final long serialVersionUID=1;
	public String filetype(){return "txt";}
	public String contenttype(){return "plain/text";}
	public String lastmod(){return null;}
	public long lastmodupdms(){return 24*60*60*1000;}
	public boolean cacheforeachuser(){return false;}

	public void to(final xwriter x)throws Throwable{
		final path p=b.path("/qa/t001.txt");
		final ByteBuffer bb=ByteBuffer.wrap(new byte[(int)p.size()]);
		p.to(bb);
		bb.flip();
		x.outputstream().write(bb.array(),bb.position(),bb.remaining());
	}
}