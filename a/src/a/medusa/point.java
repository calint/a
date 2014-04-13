package a.medusa;

import a.medusa.medusa.inline;
import a.medusa.medusa.reads;
import a.medusa.medusa.self;


public class point{
	public float x,y,z;
	
	public point(){}
	public point(final float x,final float y,final float z){this.x=x;this.y=y;this.z=z;}
//	public point(final@readonly point copy){this.x=copy.x;this.y=copy.y;this.z=copy.z;}
	public point add(final@reads point delta,final float scale){
		x+=delta.x*scale;y+=delta.y*scale;z+=delta.z*scale;//? simd		
		return this;
	}
	public@self point norm(){return scale(1);}
	public@self point scale(final float size){
		final float len=sqrtf(x*x+y*y+z*z);
		if(len==0)return this;
		final float len_inv=size/len;
		x*=len_inv;y*=len_inv;z*=len_inv;//? simd
		return this;
	}
	public static final@inline float sqrtf(final float f){return(float)Math.sqrt(f);}
}
