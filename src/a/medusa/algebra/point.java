package a.medusa.algebra;

import a.medusa.medusa.inline;
import a.medusa.medusa.reads;
import a.medusa.medusa.self;


public class point implements vector{
	public float x,y,z;
	
	public point(){}
	public point(final float x,final float y,final float z){this.x=x;this.y=y;this.z=z;}
	public point(final@reads point origo,final@reads point p){
		x=p.x-origo.x;y=p.y-origo.y;z=p.z-origo.z;//? 2 ram reads, 1 simd op, 1 ram write
	}
	public String toString(){return "|"+x+"  "+y+"  "+z+"|";}

	final public point inc(final@reads point delta,final float scale){
		x+=delta.x*scale;y+=delta.y*scale;z+=delta.z*scale;//? simd		
		return this;
	}
	final public@self point norm(){return scale(1);}
	final public@self point scale(final float size){
		final float len=sqrtf(x*x+y*y+z*z);
		if(len==0)return this;
		final float len_inv=size/len;
		x*=len_inv;y*=len_inv;z*=len_inv;//? simd
		return this;
	}
	final public float dot(final@reads point v){
		return v.x*x+v.y*y+v.z*v.z;//? 1 op in hw
	}
	// stores result in this vector
	final public@self point cross(final@reads point v){
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
	
	
	public static final@inline float sqrtf(final float f){return(float)Math.sqrt(f);}
}