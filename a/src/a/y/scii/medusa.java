package a.y.scii;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class medusa implements Serializable{
	public static class screen implements Serializable{
		public int wi,hi;
		ByteBuffer bb;
		public screen(final int width,final int height){
			wi=width;hi=height;
			bb=ByteBuffer.allocate(height*width);
		}
		
		int cursor;
		public void screen_to_outputstream(final OutputStream os)throws IOException{
			int ix=0;
			byte[]ba=bb.array();
			final int w=wi;
			for(int i=0;i<hi;i++){
				os.write(ba,ix,w);
				os.write(new byte[]{'\n'});
				ix+=w;
			}
		}
		public void cursor_place(final int row,final int col){
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
	public screen scr;
	public int frame;
	public medusa(){
		scr=new screen(80,40);
	}
	public void rst(){
		scr.rst();
		frame=0;
		sprites.clear();
	}
	public void update(){sprites.forEach(sprite::update);}
	public void draw(final screen s){sprites.forEach((sprite sp)->sp.draw(s));}
	
	public static class sprite_image{
		String[]scan_lines=new String[]{
				" ____ ",
				"|O  O|",
				"|_  _|",
				" /||\\ "
			};
		public int wi,hi;
		public void load(){
			hi=scan_lines.length;
			wi=scan_lines[0].length();
		}
		public void draw_to_screen(final screen s,final int x,final int y){
			final int h=s.hi;
			if(y>=h)return;
			int r=y,c=x;
			for(String ln:scan_lines){
				s.cursor_place(r++,c);
				if(r==h)break;
				s.print(ln);
			}
		}
	}
	public static class sprite{
		public sprite_image sprite_image;
		public float x,y;
		public void update(){
			x+=1;
			y+=(float)Math.random();			
		}
		public void draw(final screen s){
			if(sprite_image==null)return;
			sprite_image.draw_to_screen(s,Math.round(x),Math.round(y));
		}
	}
	public final ArrayList<sprite>sprites=new ArrayList<>(128);
	private static final long serialVersionUID=1L;
}