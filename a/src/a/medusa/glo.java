package a.medusa;

import java.io.Serializable;

import a.medusa.medusa.gives;
import a.medusa.medusa.readonly;

public class glo implements Serializable{
	public void load(){}
	public void draw_to_screen(final screen s,final@readonly float[]xy,final float angle){}

	////////////////////////
	public static void add2(final float[]dest_xy,final@readonly float[]xy){
		dest_xy[0]+=xy[0];dest_xy[1]+=xy[1];//? simd
	}
	public static void add2(final float[]dest_xy,final@readonly float[]xy,final float scale){
		dest_xy[0]+=scale*xy[0];dest_xy[1]+=scale*xy[1];//? simd
	}
	public static@gives float[]vertices_circle_xy(final int points,final@readonly float[]scale_xy){
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
	public static void normalize(final float[]xy,final float length){
		final float len=(float)Math.sqrt(xy[0]*xy[0]+xy[1]*xy[1]);
		if(len==0)return;
		final float leninv=length/len;
		xy[0]*=leninv;xy[1]*=leninv;///simd
	}

	private static final long serialVersionUID = 1L;

}