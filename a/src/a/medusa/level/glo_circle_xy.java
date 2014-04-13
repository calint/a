package a.medusa.level;

import a.any.list.read;
import a.medusa.glo;
import a.medusa.medusa;
import a.medusa.screen;
import a.medusa.algebra.point;

public class glo_circle_xy implements glo{
	@Override public void load(){
		vertices_xy=medusa.make_vertices_circle_xy(12,new float[]{10,10});
	}

	@Override public void draw(screen s,@read point p,float angle){
		if(vertices_xy_world_coords_cache==null){
			vertices_xy_world_coords_cache=new float[vertices_xy.length];
		}
		//? cache
		medusa.vertices_rotate_about_z_axis(vertices_xy_world_coords_cache,vertices_xy,angle,new float[]{p.x,p.y});
		s.render_convex_polygon(vertices_xy_world_coords_cache,vertices_xy_world_coords_cache.length>>1,(byte)'#',false);
		s.render_dots(vertices_xy_world_coords_cache,vertices_xy_world_coords_cache.length>>1,(byte)'#');
	}

	
	float[]vertices_xy;
	float[]vertices_xy_world_coords_cache;
	private static final long serialVersionUID=1L;
}
