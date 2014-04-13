package a.medusa;

import a.medusa.algebra.planes;
import a.medusa.medusa.reads;

public class planes_cache extends planes{
	public planes_cache(final@reads planes v){
		volume_to_cache=v;
	}
	@Override public int check_collision_with_sphere(final glob holder_of_this_volume,final@reads float[]pos,final float bounding_radius){
		//. update cached volume if glob has updated angle
		return cached_volume.check_collision_with_sphere(holder_of_this_volume,pos,bounding_radius);
	}
	@Override public boolean check_collision_with_planes(@reads final planes v){
		return false;
	}
	final private@reads planes volume_to_cache;
	final private planes cached_volume=new planes();
	private int planes_world_coords_cache_frame;
}
