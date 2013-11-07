package a.x;
import java.io.IOException;
import java.io.OutputStream;
public final class osboundary extends OutputStream{
	private byte[] boundary_b;
	private int k;
	private OutputStream os;
	public osboundary(OutputStream os,byte[]boundaryB){
		this.os=os;
		boundary_b=boundaryB;
	}
	final public void write(int c)throws IOException{throw new Error("cannot");}
	final public void write(byte[]c)throws IOException{this.write(c,0,c.length);}
	public void write(byte[] bytes,int off,int len) throws IOException{
		int i=off;
		int end=off+len;
		while(i<end){
			byte b=bytes[i++];
			if(boundary_b[k]==b){
				k++;
				if(k==boundary_b.length){
					os.write(bytes,off,i-off);
					signal.eos();
				}
			}else{
				k=0;
				if(boundary_b[0]==b)
					k++;
			}
		}
		os.write(bytes,off,len);
	}
}
