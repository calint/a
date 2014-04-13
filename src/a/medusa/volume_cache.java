package a.medusa;

import a.medusa.math.volume;
import a.medusa.medusa.reads;

public class volume_cache extends volume{
	public volume_cache(final@reads volume v){
		volume_to_cache=v;
	}
	@Override public int check_collision_with_sphere(final glob holder_of_this_volume,final@reads float[]pos,final float bounding_radius){
		//. update cached volume if glob has updated angle
		return cached_volume.check_collision_with_sphere(holder_of_this_volume,pos,bounding_radius);
	}
	@Override public boolean check_collision_with_volume(@reads final volume v){
		return false;
	}
	final private@reads volume volume_to_cache;
	final private volume cached_volume=new volume();
	private int planes_world_coords_cache_frame;
}
