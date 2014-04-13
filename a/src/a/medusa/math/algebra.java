package a.medusa.math;

import a.medusa.medusa.reads;

final public class algebra{
	public static interface vector{}
	public static float distance_from_plane_to_point(@reads plane pn,@reads point p){return pn.distance_to_point(p);}
}
