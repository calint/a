package a.x;
import java.io.IOException;
import java.io.OutputStream;
public final class osvoid extends OutputStream{
	final public void write(final int c)throws IOException{}
	final public void write(final byte[]c)throws IOException{}
	public void write(final byte[]c,final int off,final int len)throws IOException{}
	final public static osvoid i=new osvoid();
}
