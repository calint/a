package a.medusa;

import java.io.Serializable;

import b.xwriter;
import a.medusa.algebra.point;

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

	/// textalize
	public void to(final xwriter x){
		x.p("{position:");
		position.to(x);
		x.p(",dposition_over_dt:");
		dposition_over_dt.to(x);
		x.p(",angle:").p(angle).p(",dangle_over_dt:").p(dangle_over_dt).p("}");
	}
}