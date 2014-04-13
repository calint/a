package a.medusa;

import java.io.Serializable;
import java.nio.ByteBuffer;

import a.medusa.medusa.copyatchange;
import a.medusa.medusa.reads;
import a.medusa.medusa.takes;
import a.medusa.algebra.matrix;
import a.medusa.algebra.planes;
import a.medusa.algebra.point;
import b.xwriter;

public class glob implements Serializable{
	final public static point origo=new point();
	
	public void draw(final screen s,final medusa m){
		if(glo!=null){
			if(physics!=null){
				glo.draw(s,physics.position,physics.angle.z);
			}else{
				glo.draw(s,origo,0);				
			}
		}
	}
	public void tick(final float dt,final medusa m){
		if(physics!=null)physics.tick(dt);
	}
	public void on_msg(final ByteBuffer bb,final medusa mds)throws Throwable{}
	final public glob xy(final float x, final float y){physics.position.x=x;physics.position.y=y;return this;}
	final public glob glo(final glo g){glo=g;return this;}
	final public glob da(float radians){physics.dangle_over_dt.z=radians;return this;}	
	static boolean check_collision(final glob a,final glob b){
		return false;
	}
	
	// components add by subclasses
	protected@takes physics physics;
	protected@takes planes volume;
	protected@copyatchange glo glo;
	
	protected lazy_matrix model_to_local_matrix;// local scale,rotation,translation matrix
	protected lazy_chain_matrix model_to_world_matrix;// cached model to world transform
	protected lazy_chain_matrix model_to_world_normals_matrix;// contains rotations
	final public point vector_axis_z(){return null;}
	final public point vector_axis_y(){return null;}
	final public point vector_axis_x(){return null;}
	final @reads matrix matrix_model_to_local(){
		if(model_to_local_matrix==null)return null;
		if(physics==null)return model_to_local_matrix.refresh_and_get(scale,point.origo,point.origo);
		return model_to_local_matrix.refresh_and_get(scale,physics.angle,physics.position);
	}
	private final point scale=new point(1,1,1);
	
	public void to(final xwriter x){
		x.p(getClass().getName()).p("{physics:");
		if(physics!=null)physics.to(x);
		x.p(",volume:");
		if(volume!=null)volume.to(x);
		x.p("}");
	}
	private static final long serialVersionUID=1;
}