package a.medusa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class medusa implements Serializable{
//	public screen scr;
	public int frame;
	public medusa(){
//		scr=new screen(80,40);
	}
	public void rst(){
//		scr.rst();
		frame=0;
		players_all.clear();
		
		final sprite spi=new sprite();
		spi.load();
		
		player sp=new player().sprite(spi);sp.xy(29,2);
		players_all.add(sp);
		
		sp=new player().sprite(spi);sp.xy(30,8);
		players_all.add(sp);
		
		sp=new player().sprite(spi);sp.xy(70,30);
		players_all.add(sp);

		sp=new player().sprite(spi);sp.xy(53,34);
		players_all.add(sp);

		players_free.clear();
		players_free.addAll(players_all);		
	}
//	long last_dt_ms=System.currentTimeMillis();
	float[]vertices_xy;
	float[]vertices_minute_xy;
	float a;
	float a_mins;
	float dots=3;
	float ddots=.5f;
//	float dt;
	public void update(final float dt){
		last_update_dt_s=dt;
		players_all.forEach((glob s)->s.update(dt));

//		vertices_xy=vertices_circle_xy(128,new float[]{15,10});
		vertices_xy=vertices_circle_xy((int)dots,new float[]{15,10});
		dots+=ddots*dt;
		if(dots>30)dots=3;
		vertices_rotate_about_z_axis(vertices_xy,vertices_xy,a,new float[]{20,20});
		final float da=(float)Math.PI*2/60;
		a+=da*dt;
//		a=(float)(sprites.get(0).phys.pos[0]*Math.PI/180*4);
		
		final player s=players_all.get(0);
		vertices_minute_xy=vertices_circle_xy(3,new float[]{s.sp>0?4:3,s.sp>0?4:3});
		a_mins=-s.a;
//		vertices_rotate_about_z_axis(vertices_minute_xy,vertices_minute_xy,a_mins,new float[]{34,5});
		final physics p=s.phys;
//		System.out.println(p.pos[0]+"  "+p.pos[1]);
		vertices_rotate_about_z_axis(vertices_minute_xy,vertices_minute_xy,a_mins,new float[]{p.pos[0]/2+1,p.pos[1]+2});
		final float da_mins=(float)Math.PI*2/60/5;
		a_mins+=da_mins*dt;
	}
//	public void draw(){draw(scr);}
	static void add2(final float[]dest_xy,final float[]xy){
		dest_xy[0]+=xy[0];dest_xy[1]+=xy[1];//? simd
	}
	static void add2(final float[]dest_xy,final float[]xy,final float scale){
		dest_xy[0]+=scale*xy[0];dest_xy[1]+=scale*xy[1];//? simd
	}
	static final void vertices_rotate_about_z_axis(final float[]dst,final@readonly float[]src,final float angle_in_radians,final@readonly float[]translation){
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
//	private float[]dot=new float[]{40,20};
//	private float[]ddot=new float[]{.5f,.5f};
	public final static class p2{
		public float x,y;/*also accessed as float[2]?*/
	}
	static@gives float[]vertices_circle_xy(final int points,final@readonly float[]scale_xy){
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
	public void draw(final screen s){
		s.clear('.');
		s.render_convex_polygon(vertices_xy,vertices_xy.length>>1,(byte)'#',false);
		s.render_dots(vertices_xy,vertices_xy.length>>1,(byte)'X');
//		s.render_dot(dot,(byte)'X');		
//		s.render_convex_polygon(vertices_minute_xy,vertices_minute_xy.length>>1,(byte)'*',true);
		s.render_dots(vertices_minute_xy,vertices_minute_xy.length>>1,(byte)'X');
//		s.render_dot(dot,(byte)'X');
		
		//
		players_all.forEach((glob sp)->sp.draw(s));
	}
	public player alloc_sprite_for_new_player(){
		if(players_free.isEmpty())return null;
		return players_free.removeFirst();
	}
	public void on_player_closed_connection(player s){
		players_free.add(s);
	}
	public final ArrayList<player>players_all=new ArrayList<>(128);
	public final LinkedList<player>players_free=new LinkedList<>();
	public float last_update_dt_s;
	
	public static @interface readonly{}
	public static @interface takes{}
	public static @interface gives{}
	private static final long serialVersionUID=1L;
}