package a.medusa;

import java.io.Serializable;

public class physics implements Serializable{
	final float[]position=new float[]{0,0};
	final float[]dposition_over_dt=new float[]{0,0};
	float angle;
	float dangle_over_dt;
	public void tick(final float dt){
		glo.add2(position,dposition_over_dt,dt);
		angle+=dangle_over_dt*dt;
	}
	private static final long serialVersionUID = 1L;
}