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
	private int cursor_x;//,cursor_y;
	public void cursor_place(final int row,final int col){
		cursor_x=col;;//cursor_y=row;
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

	final byte default_char=(byte)'o';
	public void render_dot(final float[]xy,final int offset){
		final float x=xy[offset];
		final float y=xy[offset+1];
		if(x<0)return;
		if(x>wi)return;
		if(y<0)return;
		if(y>hi)return;
		bb.array()[hi*(int)y+(int)x]=default_char;
	}
	public void render_rect(final float[]xy,final float[]wh){
		final int x=(int)xy[0];
		final int y=(int)xy[1];
		final int w=(int)wh[0];
		final int h=(int)wh[1];
		//? clip and cull
		if(x<0)return;
		if(x>wi)return;
		if(y<0)return;
		if(y>hi)return;
		//
		final byte[]ba=bb.array();
		final int limy=y+h;
		final int limx=x+w;
		int pr=y*wi+x;
		int p=pr;
		for(int r=y;r<limy;r++){
			for(int c=x;c<limx;c++){
				ba[p]=default_char;
				p++;
			}
			p=pr+=wi;//? stride
		}
	}

	public void render_convex_polygon(final float[]xy){
		
	}

}