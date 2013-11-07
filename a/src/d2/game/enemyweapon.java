package d2.game;
import d2.object;
import d2.world;
abstract class enemyweapon extends weapon{
	public enemyweapon(world w,double x,double y,double a,double dx,double dy,double da,double lifetime){
		super(w,x,y,a,dx,dy,da,lifetime);
	}

	public boolean interestedOfCollisionWith(object o){
		if(o instanceof ship) return true;
		return super.interestedOfCollisionWith(o);
	}

	protected boolean handlecollision(object o){
		if(o instanceof ship){
			die();
			return false;
		}
		return super.handlecollision(o);
	}
}
