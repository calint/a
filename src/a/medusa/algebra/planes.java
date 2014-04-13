package a.medusa.algebra;

import java.util.ArrayList;

import a.medusa.glob;
import a.medusa.medusa.reads;
import a.medusa.medusa.self;
import a.medusa.medusa.takes;

public class planes{
	// 1: totally inside   2: totally outside   3: intersects
	public int check_collision_with_sphere(final glob holder_of_this_volume,final@reads float[]pos,final float bounding_radius){
		return 0;
	}
	public boolean check_collision_with_planes(final@reads planes v){
		return false;
	}
	final public @self planes planes_add(final@takes plane p){
		list.add(p);
		return this;
	}
	final private ArrayList<plane>list=new ArrayList<>(6);
}
