package a.sokio;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import b.sockio;
final class bufx{
	public bufx(){}
	public bufx(final int chunksize_B){balen=chunksize_B;}
	public bufx put(final byte[]b){return b(b,0,b.length);}
	public bufx b(final byte[]b,int off,int len){
		if(ba==null){
			ba=new byte[balen];
			i=0;n++;
		}
		final int cap=ba.length-i;
		if(cap>=len){
			System.arraycopy(b,off,ba,i,len);
			i+=len;
		}else{
			System.arraycopy(b,off,ba,i,cap);
			len-=cap;
			off+=cap;
			lsb.add(ba);
			n++;
			ba=new byte[balen];
			while(len>ba.length){
				System.arraycopy(b,off,ba,0,ba.length);
				len-=ba.length;
				off+=ba.length;				
				lsb.add(ba);
				n++;
				ba=new byte[balen];
			}
			if(len!=0){
				System.arraycopy(b,off,ba,0,len);
				i=len;
				len-=len;
				off+=len;
			}
		}
		return this;
	}
	public void to(final OutputStream os)throws Throwable{
		final Iterator<byte[]>it=lsb.iterator();
		while(true){
			n--;
			if(n==0)break;
			os.write(it.next());
		}
		os.write(ba,0,i);
	}
	public void clear(){
		lsb.clear();
		ba=null;
		i=n=0;
	}
	public boolean send_start(final sockio sc)throws Throwable{
		bboa=new ByteBuffer[n];
		final Iterator<byte[]>it=lsb.iterator();
		int x=0;
		bboa_rem=0;
		while(true){
			n--;
			if(n==0)break;
			final ByteBuffer bb=ByteBuffer.wrap(it.next());
			bboa[x++]=bb;
			bboa_rem+=bb.remaining();
		}
		final ByteBuffer bb=ByteBuffer.wrap(ba,0,i);
		bboa[x++]=bb;
		bboa_rem+=bb.remaining();
		return send_resume(sc);
	}
	public boolean send_resume(final sockio sc)throws Throwable{
		while(true){
			if(bboa_rem==0){bboa=null;clear();return true;}
			final long c=sc.write(bboa);
			if(c==0)return false;
			bboa_rem-=c;
		}
	}
	final private List<byte[]>lsb=new ArrayList<byte[]>();
	private byte[]ba;
	private int balen=16;
	private int n,i;
	private ByteBuffer[]bboa;
	private long bboa_rem;
}
