package d2.game;
import d2.world;
class shotgunmissile extends missile{
	public shotgunmissile(world w,double x,double y,double vel,double agl,int i,double spread){
		super(w,x,y,vel,agl,1);
		double a=agl-spread/2.0;
		double da=spread/i;
		while(i-->0){
			w.add(new missile(w,x+Math.random()*3,y+Math.random()*3,vel+(Math.random()*vel*.5),a,1));
			a+=da;
		}
	}
}
