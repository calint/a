package d2.game;
/* fSpiningMine - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;

class spinningmine extends friendlyweapon{
	protected static polyh ourPolygonSolid;

	public spinningmine(world w,double x,double y,double vel,double a){
		super(w,x,y,a,vel*Math.cos(a),vel*Math.sin(a),3,6);
		this.boundingradius(3.0);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(new polyh(ourPolygonSolid)));
	}

	protected static void makePolygonSolid(){
		double[] vx={-3.0,-1.0,0.0,1.0,3.0,1.0,0.0,-1.0};
		double[] vy={0.0,1.0,3.0,1.0,0.0,-1.0,-3.0,-1.0};
		int[] is={0,1,2,3,4,5,6,7};
		poly[] pl=new poly[1];
		pl[0]=new poly(is,new Color(128,40,50));
		ourPolygonSolid=new polyh(vx,vy,pl);
	}

	public boolean interestedOfCollisionWith(object var_fObject){
		return false;
	}

	public void update(double d){
		super.update(d);
		if(time>3.0){
			dx=0.0;
			dy=0.0;
			dagl-=0.1;
			polyhi.scalePoints(0.95,0.95);
			this.boundingradius(this.boundingradius()*0.95);
		}else if(time>1.0){
			dx=0.0;
			dy=0.0;
			dagl+=0.1;
			polyhi.scalePoints(1.05,1.05);
			this.boundingradius(this.boundingradius()*1.05);
		}
	}
}
