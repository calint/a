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
	public void render_dot(final float[]xy,final byte data){
		final float x=xy[0];
		final float y=xy[0+1];
		if(x<0)return;
		if(x>wi)return;
		if(y<0)return;
		if(y>hi)return;
		bb.array()[wi*(int)y+(int)x]=data;
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
//	public static class p2 implements Serializable{}
	//      example:               xy{40,20, 20,20, 40,30} 
	//		scr.render_convex_polygon(new float[]{40,20, 20,20, 40,30},3);
	//		scr.render_convex_polygon(new float[]{40,5, 20,5, 40,15},3);
	public void render_convex_polygon(final float[]xy,final int vertex_count){
		final int elems_per_vertex=2;
		// find top y
		int topy_ix=0;
		float topx=xy[0];
		float topy=xy[1];
		int i=2;
		int k=vertex_count;
		while(--k!=0){
			final float y=xy[i+1];
			if(y<topy){
				topy=y;
				topx=xy[i];
				topy_ix=i;
			}
		}
		int ix_lft,ix_rht;
		ix_lft=ix_rht=topy_ix;
		float y_lft,y_rht;
		y_lft=y_rht=topy;
		float x_lft,x_rht;
		x_lft=x_rht=topx;
		boolean adv_lft,adv_rht;
		adv_lft=adv_rht=true;
		float dxdy_lft;
		float dxdy_rht;
		dxdy_lft=dxdy_rht=0;
		float x_nxt_lft=0;
		float y_nxt_lft=topy;
		float x_nxt_rht=0;
		float y_nxt_rht=topy;
		float dy_rht=0;
		float dy_lft=0;
		float y=topy;
		while(true){
			if(adv_lft){
				y=y_lft=y_nxt_lft;
				if(ix_lft==(vertex_count-1)*elems_per_vertex)ix_lft=0;
				else ix_lft+=elems_per_vertex;
				x_nxt_lft=xy[ix_lft];
				y_nxt_lft=xy[ix_lft+1];
				dy_lft=y_nxt_lft-y_lft;
				if(dy_lft!=0)dxdy_lft=(x_nxt_lft-x_lft)/dy_lft;
				else dxdy_lft=0;
			}
			if(adv_rht){
				y=y_rht=y_nxt_rht;
//				x_rht=x_nxt_rht;
				if(ix_rht==0)ix_rht=vertex_count*elems_per_vertex-elems_per_vertex;
				else ix_rht-=elems_per_vertex;
				x_nxt_rht=xy[ix_rht];
				y_nxt_rht=xy[ix_rht+1];
				dy_rht=y_nxt_rht-y_rht;
				if(dy_rht!=0)dxdy_rht=(x_nxt_rht-x_rht)/dy_rht;
				else dxdy_rht=0;
			}
			if(adv_lft&&adv_rht){
				y=y_lft>y_rht?y_rht:y_lft;
			}
			
			int scan_lines_until_next_turn=0;
			if(y_nxt_lft>y_nxt_rht){
				scan_lines_until_next_turn=(int)(y_nxt_rht-y);
				adv_lft=false;
				adv_rht=true;
			}else{
				scan_lines_until_next_turn=(int)(y_nxt_lft-y);			
				adv_lft=true;
				adv_rht=false;
			}
			if(scan_lines_until_next_turn==0){
				x_lft=x_nxt_lft; // set x
			}
			final int y_scr=(int)y;
			int pline=y_scr*wi;
			final byte[]ba=bb.array();
			while(true){// render scan line
				int npx=(int)(x_rht-x_lft);// pixels to render
				final float startx;
				System.out.println(npx+"   "+x_lft+"   "+x_rht+"   "+dxdy_lft+"   "+dxdy_rht);
				if(npx<0){
					npx=-npx;//? temp
					startx=x_rht;
					//throw new Error();//? should be culled?
				}else
					startx=x_lft;
				int p=pline+(int)startx;// start index
				while(npx--!=0){ba[p++]=default_char;}
				pline+=wi;	
				if(scan_lines_until_next_turn==0)break;
				scan_lines_until_next_turn--;
				x_lft+=dxdy_lft;
				x_rht+=dxdy_rht;
			}
			if(ix_lft==ix_rht)
				break;// render done

		}
	}
}