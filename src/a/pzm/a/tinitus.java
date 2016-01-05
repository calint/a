package a.pzm.a;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import b.threadedsock;
import b.websock;
final public class tinitus extends websock implements threadedsock{
	long sleep_ms=1000/24;
	final protected void onopened()throws Throwable{
//		send_binary(refresh_display,png);
	}
	protected void onmessage(final ByteBuffer bb) throws Throwable{
		final ByteBuffer bo=ByteBuffer.allocate(1024*4);
		final FloatBuffer fb=bo.asFloatBuffer();
		int n=1024;
		while(n--!=0){
			fb.put((float)Math.random());
		}
		fb.flip();
		send_binary(bo);
//		switch(bb.get()){
//		case'f'://faster
//			break;
//		case's'://slower
//			break;
//		case'o'://ok
//			break;
//		default:throw new Error();
//		}
	}
//	private static final byte[]refresh_display=new byte[]{'0'};
//	public static String tostr(final ByteBuffer bb){try{return new String(bb.array(),bb.position(),bb.remaining(),strenc);}catch(Throwable t){throw new Error(t);}}
//	
	private static final long serialVersionUID=1;

}
