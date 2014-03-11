package d2.game;
import d2.world;
class controlledcluster extends clustermissile{
	protected double mySpeed;
	protected aobjectm theMaster;

	public controlledcluster(world w,double x,double y,double vel,double agl,int i,aobjectm owner){
		super(w,x,y,vel,agl,i,3,3);
		mySpeed=vel;
		theMaster=owner;
	}

	public void update(double dt){
		super.update(dt);
		dangle(theMaster.angle());
		velocity(mySpeed);
		mySpeed+=2*dt;
	}
}
