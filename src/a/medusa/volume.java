package a.medusa;

import java.util.ArrayList;

import a.medusa.medusa.readonly;

public class volume {
	float bounding_radius;
//	float bounding_radius2;
	ArrayList<plane>planes;
	ArrayList<plane>planes_world_coords_cache;
	int planes_world_coords_cache_frame;
	
	// 1: totally inside   2: totally outside   3: intersects
	int check_collision_with_sphere(final glob holder_of_this_volume,final@readonly float[]pos,final float bounding_radius){
		return 0;
	}
	boolean check_collision_with_volume(final@readonly float[]pos,final volume v){
		return false;
	}
}
