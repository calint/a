package a.y.scii;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class medusa implements Serializable{
	public static class screen implements Serializable{
		public int wi,hi;
		ByteBuffer bb;
		public screen(final int width,final int height){
			wi=width;hi=height;
			bb=ByteBuffer.allocate(height*width);
		}
		
		private static final long serialVersionUID=1L;
	}
	public screen scr;
	public int cursor;
	public medusa(){
		scr=new screen(80,40);
	}
	public void screen_to_outputstream(final OutputStream os)throws IOException{
		int ix=0;
		byte[]ba=scr.bb.array();
		final int w=scr.wi;
		for(int i=0;i<scr.hi;i++){
			os.write(ba,ix,w);
			os.write(new byte[]{'\n'});
			ix+=w;
		}
	}
	public void cursor_place(final int row,final int col){
		cursor=row*(scr.wi)+col;
	}
	public void print(final String cs){
		final byte[]ba=cs.getBytes();
		scr.bb.position(cursor);
		scr.bb.put(ba);
	}
	private static final long serialVersionUID=1L;
}