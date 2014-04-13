package a.medusa.algebra;

import java.util.ArrayList;

import a.medusa.glob;
import a.medusa.medusa.reads;

public class volume{
	// 1: totally inside   2: totally outside   3: intersects
	public int check_collision_with_sphere(final glob holder_of_this_volume,final@reads float[]pos,final float bounding_radius){
		return 0;
	}
	public boolean check_collision_with_volume(final@reads volume v){
		return false;
	}
	private final sphere bounding_sphere=new sphere(1);
//	float bounding_radius2;
	private ArrayList<plane>planes;
}
