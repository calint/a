package d2.game;
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;

class homingmissile extends enemyweapon{
	protected double speed;
	protected aobjectm theTarget;
	protected static polyh ourPolygonSolid;
	protected double myMaximumTurningAngle;

	public homingmissile(world w,double x,double y,double agl,double vel,double turnAngle,aobjectm target){
		super(w,x,y,agl,vel*Math.cos(agl),vel*Math.sin(agl),0, 4);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(ourPolygonSolid));
		this.boundingradius(2.0);
		theTarget=target;
		speed=vel;
		myMaximumTurningAngle=turnAngle;
	}

	protected static void makePolygonSolid(){
		double[] ds={1.0,-0.87,-0.87};
		double[] ds_4_={0.0,0.5,-0.5};
		int[] is={0,1,2};
		poly[] var_fPolygons=new poly[1];
		var_fPolygons[0]=new poly(is,new Color(0,100,20));
		ourPolygonSolid=new polyh(ds,ds_4_,var_fPolygons);
		ourPolygonSolid.scalePoints(2.0,2.0);
	}

	public boolean interestedOfCollisionWith(object var_fObject){
		if(var_fObject instanceof weapon) return true;
		return super.interestedOfCollisionWith(var_fObject);
	}

	protected boolean handlecollision(object var_fObject){
		if(var_fObject instanceof weapon){
			this.die();
			return false;
		}
		return super.handlecollision(var_fObject);
	}

	public void update(double d){
		super.update(d);
		double d_5_=getAngleToTarget();
		double d_6_=d_5_-agl;
		if(d_6_>0.0)
			d_5_=(d_6_>myMaximumTurningAngle?agl+myMaximumTurningAngle:agl+d_6_);
		else
			d_5_=(-d_6_>myMaximumTurningAngle?agl-myMaximumTurningAngle:agl+d_6_);
		this.angle(d_5_);
		this.angleandvelocity(d_5_,speed);
		speed+=2.0;
	}

	protected double getAngleToTarget(){
		double d=theTarget.x()-x;
		double d_7_=Math.atan((theTarget.y()-y)/d);
		if(d>0.0) return d_7_;
		return d_7_+3.141592653589793;
	}
}
