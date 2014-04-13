package a.medusa;

import java.io.Serializable;

import b.xwriter;
import a.medusa.algebra.point;

public class physics implements Serializable{
	final point position=new point();
	final point dposition_over_dt=new point();
	final point angle=new point();
	final point dangle_over_dt=new point();
	public void tick(final float dt){
		position.inc(dposition_over_dt,dt);
		angle.inc(dangle_over_dt,dt);
	}
	private static final long serialVersionUID = 1L;

	/// textalize
	public void to(final xwriter x){
		x.p("{p");position.to(x);x.p("dp");dposition_over_dt.to(x);
		x.p("a");angle.to(x);x.p(",da");dangle_over_dt.to(x);x.p("}");
	}
}