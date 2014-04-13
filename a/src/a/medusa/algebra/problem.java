package a.medusa.algebra;

import java.io.Serializable;

import a.medusa.medusa.reads;

final public class problem implements Serializable{
	public static float distance_from_plane_to_point(@reads plane pn,@reads point p){return pn.distance_to_point(p);}
	public static class distance_from_plane_to_point_solution{
		final float distance=0;
	}
	
	private static final long serialVersionUID=1;
}
