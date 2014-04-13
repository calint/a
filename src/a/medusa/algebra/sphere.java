package a.medusa.algebra;

import b.xwriter;

public class sphere {
	public sphere(final float radius){this.radius=radius;}
	public String toString(){return "sph{"+radius+"}";}
	private float radius;


	/// textilize auto
	public void to(final xwriter x){x.p("sph{").p(radius).p("}");}
}
