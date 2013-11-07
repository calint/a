package a.x;
import java.io.IOException;
import java.io.OutputStream;
public final class oscounter extends OutputStream{
	public long count=0;
	private OutputStream os;
	public oscounter(OutputStream os){this.os=os;}
	@Override final public void write(int c)throws IOException{throw new Error("cannot");}
	@Override final public void write(byte[]c)throws IOException{this.write(c,0,c.length);}
	@Override public void write(byte[] c,int off,int len)throws IOException{os.write(c,off,len);count+=len;}
}
