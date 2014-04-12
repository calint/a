package a.medusa;

public class glo_circle_xy extends glo{
	@Override public void load(){
		vertices_xy=glo.vertices_circle_xy(12,new float[]{10,10});
	}

	@Override public void draw_to_screen(screen s,float[]xy,float angle){
		if(vertices_xy_world_coords_cache==null){
			vertices_xy_world_coords_cache=new float[vertices_xy.length];
		}
		//? cache
		glo.vertices_rotate_about_z_axis(vertices_xy_world_coords_cache,vertices_xy,angle,xy);
		s.render_convex_polygon(vertices_xy_world_coords_cache,vertices_xy_world_coords_cache.length>>1,(byte)'#',false);
		s.render_dots(vertices_xy_world_coords_cache,vertices_xy_world_coords_cache.length>>1,(byte)'#');
	}

	
	float[]vertices_xy;
	float[]vertices_xy_world_coords_cache;
	private static final long serialVersionUID=1L;
}
