package a.x;
import java.io.IOException;
import java.io.OutputStream;
public final class osdelay extends OutputStream{
	private byte[]delay_b;
	private int k=0;
	private OutputStream os;
	public osdelay(OutputStream os,int len){
		this.os=os;
		delay_b=new byte[len];
	}
	@Override final public void write(int c)throws IOException{throw new Error("cannot");}
	@Override final public void write(byte[]c)throws IOException{this.write(c,0,c.length);}
	@Override public void write(byte[] c,int off,int len) throws IOException{
		int total=k+len;
		if(total<=delay_b.length){
			System.arraycopy(c,off,delay_b,k,len);
			k+=len;
			return;
		}
		int dl=len-delay_b.length;
		if(dl>=0){
			if(k>0)
				os.write(delay_b,0,k);
			if(dl>0)
				os.write(c,off,dl);
			System.arraycopy(c,off+dl,delay_b,0,delay_b.length);
			k=delay_b.length;
			return;
		}
		int diff=total-delay_b.length;
		os.write(delay_b,0,diff);
		System.arraycopy(delay_b,diff,delay_b,0,delay_b.length-diff);
		System.arraycopy(c,off,delay_b,delay_b.length-diff,diff);
		k=delay_b.length;
		return;
	}
}
