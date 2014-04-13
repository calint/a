package a.medusa;

import java.io.Serializable;

import a.medusa.math.point;

public class physics implements Serializable{
	final point position=new point();
	final point dposition_over_dt=new point();
	float angle;
	float dangle_over_dt;
	public void tick(final float dt){
		position.inc(dposition_over_dt,dt);
		angle+=dangle_over_dt*dt;
	}
	private static final long serialVersionUID = 1L;
}