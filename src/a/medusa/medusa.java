package a.medusa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

final public class medusa extends glob implements Serializable{
	public static@conf String app="a.medusa.level.ctor";
	final public void reset()throws Throwable{
		players_all.clear();
		Class.forName(app).asSubclass(app.class).newInstance().init(this);
		players_free.clear();
		players_free.addAll(players_all);		
	}
	@readonly long frame;
	final@Override public void tick(final float dt,final medusa m){
//		last_update_dt_s=dt;
		frame++;
//		players_all.forEach(g->g.tick(dt));
		players_all.parallelStream().forEach(g->g.tick(dt,m));
	}
	final@Override public void draw(final screen s,final medusa m){
		s.clear('.');
		players_all.forEach(g->g.draw(s,m));
	}
	final public player players_get_free_for_play(){if(players_free.isEmpty())return null;return players_free.removeFirst();}
	final void on_closed_connection(final player p){players_free.add(p);}
	private final ArrayList<player>players_all=new ArrayList<>(128);
	final public medusa players_add(final@takes player p){players_all.add(p);return this;}
	final public player players_get(final int id){return players_all.get(id);}
	private final LinkedList<player>players_free=new LinkedList<>();
	final boolean has_active_players(){return players_free.size()==players_all.size();}
//	private float last_update_dt_s;
//	final public float dt(final float s){return s*last_update_dt_s;}
	public static float dtor=(float)(Math.PI/180);
	public static float dtor(final float degrees){return degrees*dtor;}
	
	public static @interface readonly{}
	public static @interface takes{}
	public static @interface gives{}
	public static @interface conf{}
	public static interface app{void init(final medusa m)throws Throwable;}
	private static final long serialVersionUID=1L;
	final public int players_free_count(){return players_free.size();}
	final public int players_active_count(){return players_all.size()-players_free.size();}
	public static@gives float[]make_vertices_circle_xy(final int points,final@readonly float[]scale_xy){
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
	public static void vertices_rotate_about_z_axis(final float[]dst,final@readonly float[]src,final float angle_in_radians,final@readonly float[]translation){
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
	////////////////////////
	public static void v2add(final float[]dest_xy,final@readonly float[]xy){
		dest_xy[0]+=xy[0];dest_xy[1]+=xy[1];//? simd
	}
	public static void v2add(final float[]dest_xy,final@readonly float[]xy,final float scale){
		dest_xy[0]+=scale*xy[0];dest_xy[1]+=scale*xy[1];//? simd
	}
	public static void v2norm(final float[]xy,final float length){
		final float len=(float)Math.sqrt(xy[0]*xy[0]+xy[1]*xy[1]);
		if(len==0)return;
		final float leninv=length/len;
		xy[0]*=leninv;xy[1]*=leninv;///simd
	}
}