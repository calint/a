package a.qa;
import b.a;
import b.b;
import b.path;
import b.xwriter;

import java.nio.ByteBuffer;

// big chuncks reply
public class t022 extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
        final path p=b.path("qa/t001.txt");
        final ByteBuffer bb=ByteBuffer.wrap(new byte[(int)p.size()]);
        p.to(bb);
        bb.flip();
        x.outputstream().write(bb.array(),bb.position(),bb.remaining());
    }
}