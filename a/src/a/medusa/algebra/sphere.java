package a.medusa.algebra;

import b.xwriter;

public class sphere {
	public sphere(final float radius){this.radius=radius;}
	public String toString(){return "sphere{radius:"+radius+"}";}
	public void to(final xwriter x){x.p("sphere{radius:").p(radius).p("}");}

	private float radius;
}
