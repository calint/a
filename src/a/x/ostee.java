package a.x;
import java.io.IOException;
import java.io.OutputStream;
public final class ostee extends OutputStream{
	private OutputStream os[];
	public ostee(final OutputStream[]oss){this.os=oss;}
	final public void write(final int c)throws IOException{throw new Error("cannot");}
	final public void write(final byte[]c)throws IOException{write(c,0,c.length);}
	public void write(final byte[]c,final int off,final int len)throws IOException{
		for(int n=0;n<os.length;n++)
			os[n].write(c,off,len);
	}
}
