package a;
import java.nio.ByteBuffer;
import java.util.Date;
public class websock extends b.websock{static final long serialVersionUID=1;
	private int counter;
	protected op onpayload(final ByteBuffer bb, final int nbytes)throws Throwable{
		final String msg=new String(bb.array(),bb.position(),nbytes);
		bb.position(bb.position()+nbytes);
//		System.out.println(msg);
		if(!is_payload_read_done())return continue_reading_frame_payload();

		//frame payload processed
		final StringBuilder sb=new StringBuilder();
		for(int i=0;i<200000;i++)sb.append((char)b.b.rndint('a','z'));
		final ByteBuffer result=ByteBuffer.wrap((counter+++" "+msg+" "+new Date()+" "+sb).getBytes());
		return send_and_then_read_next_frame(result);
	}
}
