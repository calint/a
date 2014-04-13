package a.medusa.algebra;

import b.xwriter;

public class sphere {
	public sphere(final float radius){this.radius=radius;}
	public String toString(){return "sphere{radius:"+radius+"}";}
	private float radius;


	/// textilize auto
	public void to(final xwriter x){x.p(getClass().getName()).p("{radius:").p(radius).p("}");}
}
