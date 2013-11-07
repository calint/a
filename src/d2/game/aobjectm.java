package d2.game;
import d2.objectm;
import d2.world;
abstract class aobjectm extends objectm{
	public aobjectm(world w,double x,double y,double a,double dx,double dy,double da){
		super(w,x,y,a,dx,dy,da);
	}

	public void updatePhysics(double d){
		super.updatePhysics(d);
		if(x>180.0)
			x=20.0;
		else if(x<20.0) x=180.0;
		if(y>180.0)
			y=20.0;
		else if(y<20.0) y=180.0;
	}
}
