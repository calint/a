package a.medusa.algebra;

import java.io.Serializable;

import b.xwriter;
import a.medusa.medusa.autoset;
import a.medusa.medusa.reads;
import a.medusa.medusa.takes;

public class plane implements Serializable{
	public plane(final@takes point origo,final@reads point on_plane,final@reads point also_on_plane){
		this.point=origo;//? dislocation of data, cache line performance?
		final point v1=new point(origo,on_plane);
		final point v2=new point(origo,also_on_plane);
		v1.cross(v2);// v1 = v1 x v2
		v1.norm();
		normal=v1;
		//v2.dispose();
	}
	public@autoset plane(final@takes point origo,final@takes point normal,final boolean normalize_normal){this.point=origo;this.normal=normal;
		if(normalize_normal)normal.norm();
	}
	final public float distance_to_point(final@reads point p){
		final point v=new point(point,p);
		return normal.dot(v);
	}
	// 1: totally behind   2: totally infront   3: intersects
	final public int check_collision_with_sphere(final@reads point p,final float radius){
		final float distance_to_sphere=distance_to_point(p);
		if(distance_to_sphere>radius)return 2;//? maybe >=
		if(distance_to_sphere<-radius)return 1;//? maybe 
		return 3;
	}


	private final point point;
	private final point normal;
	
	
	/// textilize
	public void to(final xwriter x){
		//? reflection
		x.p("pn{p");point.to(x);x.p("n");normal.to(x);x.p("}");
	}
	
	
	private static final long serialVersionUID=1;
}
