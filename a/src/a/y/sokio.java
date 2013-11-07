package a.y;

import java.nio.ByteBuffer;
import java.util.Map;
import b.a;
import b.sock;
import b.sockio;

public class sokio extends a implements sock{
	static final long serialVersionUID=1;
	private sockio soi;
	private final ByteBuffer in=ByteBuffer.allocate(128);
	private final ByteBuffer out=ByteBuffer.allocate(1024);
	private int s=0;
	public op sockinit(final Map<String,String>hdrs,final sockio _)throws Throwable{
		soi=_;
		return op.write;
	}
	public op read()throws Throwable{
		in.clear();
		soi.read(in);
		in.flip();
		return op.write;
	}
	public op write()throws Throwable{
		out.clear();
		if(s==0){
			out.put(" retro text adventure game sockio\n\n u r in roome\n u c me\n exits: none\n todo: find an exit\n\nkeywords: look go back select take drop copy  say goto inventory\n".getBytes());
			s++;
		}
		out.put(("> u typed "+new String(in.array(),0,in.limit())+"\n< ").getBytes());
		out.flip();
		soi.write(out);
		return op.read;
	}
}
