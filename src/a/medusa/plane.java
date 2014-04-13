package a.medusa;

import a.medusa.medusa.reads;
import a.medusa.medusa.takes;

public class plane{
	public plane(final@takes point origo,final@reads point on_plane,final@reads point also_on_plane){
		this.origo=origo;
		final vector v1=new vector(origo,on_plane);
		final vector v2=new vector(origo,also_on_plane);
		v1.cross(v2);// v1 = v1 x v2
		v1.norm();
		normal=v1;
	}
	public plane(final@takes point origo,final@takes vector normal,final boolean normalize_normal){
		this.origo=origo;
		if(normalize_normal)normal.norm();
		this.normal=normal;
	}
	float distance_to_point(final@reads point p){
		final vector v=new vector(origo,p);
		return normal.dot(v);
	}
	// 1: totally behind   2: totally infront   3: intersects
	int check_collision_with_sphere(final@reads point p,final float radius){
		final float distance_to_sphere=distance_to_point(p);
		if(distance_to_sphere>radius)return 2;//? maybe >=
		if(distance_to_sphere<-radius)return 1;//? maybe 
		return 3;
	}


	private final point origo;
	private final vector normal;
}
