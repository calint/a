package a.medusa.algebra;

import a.medusa.medusa.autoset;
import a.medusa.medusa.reads;
import a.medusa.medusa.takes;

public class plane{
	public plane(final@takes point origo,final@reads point on_plane,final@reads point also_on_plane){
		this.origo=origo;//? dislocation of data, cache line performance?
		final point v1=new point(origo,on_plane);
		final point v2=new point(origo,also_on_plane);
		v1.cross(v2);// v1 = v1 x v2
		v1.norm();
		normal=v1;
		//v2.dispose();
	}
	public@autoset plane(final@takes point origo,final@takes point normal,final boolean normalize_normal){this.origo=origo;this.normal=normal;
		if(normalize_normal)normal.norm();
	}
	final public float distance_to_point(final@reads point p){
		final point v=new point(origo,p);
		return normal.dot(v);
	}
	// 1: totally behind   2: totally infront   3: intersects
	final public int check_collision_with_sphere(final@reads point p,final float radius){
		final float distance_to_sphere=distance_to_point(p);
		if(distance_to_sphere>radius)return 2;//? maybe >=
		if(distance_to_sphere<-radius)return 1;//? maybe 
		return 3;
	}


	private final point origo;
	private final point normal;
}
