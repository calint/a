package a.y;

import java.nio.ByteBuffer;
import java.util.Map;
import b.a;
import b.sock;
import b.sockio;

public class sokio extends a implements sock{
	static final long serialVersionUID=1;
	private sockio so;
	private final ByteBuffer in=ByteBuffer.allocate(128);
	private final ByteBuffer out=ByteBuffer.allocate(1024);
	public op sockinit(final Map<String,String>hdrs,final sockio s)throws Throwable{
		so=s;
		out.clear();
		out.put(" retro text adventure game sockio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n\n< ".getBytes());
		out.flip();
		return write();
	}
	public op read()throws Throwable{
		in.clear();
		final int c=so.read(in);
		if(c==-1)return op.close;
		if(c==0)return op.read;
		in.flip();
		out.clear();
		if(dodo()){
			out.flip();
			return write();
		}else return op.read;
	}
	public op write()throws Throwable{
		final int cc=so.write(out);if(cc==0);
		if(out.hasRemaining())
			return op.write;
		return op.read;
	}
	protected boolean dodo()throws Throwable{
		out.put("> u typed ".getBytes());
		out.put(in.array(),in.position(),in.remaining());
		out.put("\n< ".getBytes());
		return true;
	}
}
