package a.medusa;

import java.io.Serializable;

public class physics implements Serializable{
//	final float[]position=new float[]{0,0};
//	final float[]dposition_over_dt=new float[]{0,0};
	final point position=new point();
	final point dposition_over_dt=new point();
	float angle;
	float dangle_over_dt;
	public void tick(final float dt){
		position.add(dposition_over_dt,dt);
//		medusa.v2add(position,dposition_over_dt,dt);
		angle+=dangle_over_dt*dt;
	}
	private static final long serialVersionUID = 1L;
}