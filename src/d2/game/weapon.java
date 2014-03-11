package d2.game;

import d2.object;
import d2.world;

abstract class weapon extends aobjectm{
	protected double lifeTime;
	public weapon(world w,double x,double y,double a,double dx,double dy,double da,double lifetime){
		super(w,x,y,a,dx,dy,da);
		lifeTime=lifetime;
	}

	public boolean interestedOfCollisionWith(object o){
		if(o instanceof asteroid)return true;
		if(o instanceof cruiser)return true;
		return false;
	}

	protected boolean handlecollision(object o){
		if(o instanceof asteroid){
			die();
			return false;
		}
		return super.handlecollision(o);
	}

	public void update(double dt){
		super.update(dt);
		if(time>lifeTime)die();
	}
}
