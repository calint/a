package a.y.scii;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class medusa implements Serializable{
	public screen scr;
	public int frame;
	public medusa(){
		scr=new screen(80,40);
	}
	public void rst(){
		scr.rst();
		frame=0;
		sprites.clear();
		
		final image spi=new image();
		spi.load();
		
		sprite sp=new sprite().image(spi).xy(29,2);
		sprites.add(sp);
		
		sp=new sprite().image(spi).xy(30,8);
		sprites.add(sp);
		
		sp=new sprite().image(spi).xy(70,30);
		sprites.add(sp);

		sp=new sprite().image(spi).xy(53,34);
		sprites.add(sp);

		sprites_available_for_new_players.clear();
		sprites_available_for_new_players.addAll(sprites);		
	}
	public void update(){sprites.forEach(sprite::update);}

	public void draw(){draw(scr);}
	final static float dt=1/10.f;
	static void add2(final float[]dest_xy,final float[]xy){
		dest_xy[0]+=xy[0];dest_xy[1]+=xy[1];//? simd
	}
	static void add2(final float[]dest_xy,final float[]xy,final float scale){
		dest_xy[0]+=scale*xy[0];dest_xy[1]+=scale*xy[1];//? simd
	}
	static final void vertices_rotate_about_z_axis(final float[]dst,final/*readonly*/float[]src,final float angle_in_radians,final/*read*/float[]translation){
		// |cos -sin||x|=|xcos-ysin|
		// |sin  cos||y|=|xsin+ycos|
//		System.out.println(angle_in_radians*180/Math.PI);
		final float c=(float)Math.cos(angle_in_radians);
		final float s=(float)Math.sin(angle_in_radians);
		final float tx=translation[0];
		final float ty=translation[1];		
		for(int i=0;i<src.length;i+=2){
			final float x=src[i];
			final float y=src[i+1];
			final float x1=x*c-y*s+tx;
			final float y1=-(x*s+y*c)+ty;//? adjustment for y
			dst[i]=x1*2; // adjustement for 80x40 square view
			dst[i+1]=y1;
		}
	}
	private float[]dot=new float[]{40,20};
//	private float[]ddot=new float[]{.5f,.5f};
	public final static class p2{
		public float x,y;/*also accessed as float[2]?*/
	}
	static/*gives*/float[]vertices_circle_xy(final int points,final/*readonly*/float[]scale_xy){
		float a=0;
		float da=(float)(Math.PI*2/points);
		final float[]xy=new float[points*2];// 2 components/vertex
		int ix=0;
		for(int i=0;i<points;i++){
			final float x=scale_xy[0]*(float)Math.cos(a);
			final float y=scale_xy[1]*(float)Math.sin(a);
			a+=da;
			xy[ix++]=x;
			xy[ix++]=y;
		}
		return xy;
	}
	float a;
	float a_mins;
	float dots=3;
	float ddots=.5f;
	public void draw(final screen s){
		scr.clear('.');
		/// background layers
//		add2(dot,ddot);
//		scr.render_rect(new float[]{15,25},new float[]{4,3});
//		scr.render_rect(new float[]{21,24},new float[]{10,4});
//		scr.render_rect(new float[]{32,21},new float[]{10,7});
		
		final float[]vertices_xy=vertices_circle_xy((int)dots,new float[]{15,10});
		dots+=ddots*dt;
		if(dots>30)dots=3;
		vertices_rotate_about_z_axis(vertices_xy,vertices_xy,a,new float[]{20,20});
		final float da=(float)Math.PI*2/60;
		a+=da*dt;
//		a=(float)(sprites.get(0).phys.pos[0]*Math.PI/180*4);
		scr.render_convex_polygon(vertices_xy,vertices_xy.length>>1,(byte)'o');
		scr.render_dots(vertices_xy,vertices_xy.length>>1,(byte)'X');
//		scr.render_dot(dot,(byte)'X');
		
		final float[]vertices_minute_xy=vertices_circle_xy(12,new float[]{5,5});
		vertices_rotate_about_z_axis(vertices_minute_xy,vertices_minute_xy,a_mins,new float[]{33,7});
		final float da_mins=(float)Math.PI*2/60/5;
		a_mins+=da_mins*dt;
//		scr.render_convex_polygon(vertices_minute_xy,vertices_minute_xy.length>>1,(byte)'O');
		scr.render_dots(vertices_minute_xy,vertices_minute_xy.length>>1,(byte)'X');
//		scr.render_dot(dot,(byte)'X');
		
		//
		sprites.forEach((sprite sp)->sp.draw(s));
	}
	public sprite alloc_sprite_for_new_player(){
		if(sprites_available_for_new_players.isEmpty())return null;
		return sprites_available_for_new_players.removeFirst();
	}
	public void on_player_closed_connection(sprite s){
		sprites_available_for_new_players.add(s);
	}
	public final ArrayList<sprite>sprites=new ArrayList<>(128);
	public final LinkedList<sprite>sprites_available_for_new_players=new LinkedList<>();
	private static final long serialVersionUID=1L;
}