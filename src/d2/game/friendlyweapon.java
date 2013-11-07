package d2.game;

import d2.object;
import d2.world;

abstract class friendlyweapon extends weapon{
	public friendlyweapon(world w,double x,double y,double a,double dx,double dy,double da,double lifetime){
		super(w,x,y,a,dx,dy,da,lifetime);
	}

	public boolean interestedOfCollisionWith(object o){
		if(o instanceof cruiser)return true;
		return super.interestedOfCollisionWith(o);
	}

	protected boolean handlecollision(object o){
		if(o instanceof cruiser){
			die();
			return false;
		}
		return super.handlecollision(o);
	}
}
