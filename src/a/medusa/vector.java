package a.medusa;

import a.medusa.medusa.inline;
import a.medusa.medusa.reads;
import a.medusa.medusa.self;

public class vector extends point{
	public vector(final@reads point origo,final@reads point p){
		super(p.x-origo.x,p.y-origo.y,p.z-origo.z);//? simd
	}
	public float dot(final@reads vector v){
		return v.x*x+v.y*y+v.z*v.z;//? 1 op in hw
	}
	// stores result in this vector
	public@self vector cross(final@reads vector v){
		final float x1=x;final float y1=y;final float z1=z;//? 1 ram read
		final float x2=v.x;final float y2=v.y;final float z2=v.z;//? 1 ram read
		
//		result[0]=p1[1]*p2[2]-p2[1]*p1[2];
//		result[1]=p1[2]*p2[0]-p2[2]*p1[0];
//		result[2]=p1[0]*p2[1]-p2[0]*p1[1];
		
		final float nx=y1*z2-y2*z1;//? 1 op in hw
		final float ny=z1*x2-z2*x1;//?
		final float nz=x1*y2-x2*y1;//?
		
		x=nx;y=ny;z=nz;//? 1 write ram op
		
		return this;
	}
	public@self vector norm(){
		final float len=sqrtf(x*x+y*y+z*z);
		final float len_inv=1/len;
		x*=len_inv;y*=len_inv;z*=len_inv;//? simd
		return this;
	}
	public static final@inline float sqrtf(final float f){return (float)Math.sqrt(f);}
}
