package a.y.scii;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class screen implements Serializable{
	public int wi,hi;
	ByteBuffer bb;
	public screen(final int width,final int height){
		wi=width;hi=height;
		bb=ByteBuffer.allocate(height*width);
	}
	
	int cursor;
	public void screen_to_outputstream(final OutputStream os){try{
		int ix=0;
		byte[]ba=bb.array();
		final int w=wi;
		for(int i=0;i<hi;i++){
			os.write(ba,ix,w);
			os.write(new byte[]{'\n'});
			ix+=w;
		}
	}catch(IOException e){throw new Error(e);}}
	private int cursor_x,cursor_y;
	public void cursor_place(final int row,final int col){
		cursor_x=col;cursor_y=row;
		cursor=row*wi+col;
	}
	public void print(final String cs){
		final byte[]ba=cs.getBytes();
		bb.position(cursor);
		final int rem=bb.remaining();
		final int diff=rem-ba.length;
		if(diff<0)bb.put(ba,0,ba.length+diff);
		else bb.put(ba);
	}
	public void print(final String cs,final int clip_n_characters_from_start_of_line){
		final byte[]ba=cs.getBytes();
		bb.position(cursor);
		final int rem=wi-cursor_x;
		final int diff=rem-ba.length+clip_n_characters_from_start_of_line;
		if(diff<0)bb.put(ba,clip_n_characters_from_start_of_line,ba.length+diff-clip_n_characters_from_start_of_line);
		else bb.put(ba,clip_n_characters_from_start_of_line,ba.length-clip_n_characters_from_start_of_line);
	}
	private static final long serialVersionUID=1L;
	public void clear(final char c){
		final byte b=(byte)c;
		final byte[]ba=bb.array();
		for(int i=0;i<ba.length;i++)
			ba[i]=b;
	}
	public void rst(){
		bb.clear();
		bb.rewind();
	}

}