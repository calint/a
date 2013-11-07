package d2.game;
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;

class supplies extends aobjectm{
	protected int type;
	protected double lifeTime=20.0;
	protected static int[] amounts={40,20,20,7,3,4,3,10,5,2,1};
	static polyh ourPolygonSolid;

	public supplies(world w,double x,double y,double vel,double agl,int i,Color color){
		super(w,x,y,agl,vel*Math.cos(agl),vel*Math.sin(agl),1.0);
		type=i;
		if(ourPolygonSolid==null)makePolygonSolid();
		polyhi(new polyhi(new polyh(ourPolygonSolid)));
		polyhi.setPolygonColor(0,color);
		boundingradius(3);
	}

	protected static void makePolygonSolid(){
		double[] vx={2,-2,-2,2};
		double[] vy={2,2,-2,-2};
		poly[]pl=new poly[1];
		int[]ix={0,1,2,3};
		pl[0]=new poly(ix,Color.cyan);
		ourPolygonSolid=new polyh(vx,vy,pl);
	}

	public boolean interestedOfCollisionWith(object var_fObject){
		if(var_fObject instanceof ship) return true;
		return false;
	}

	protected boolean handlecollision(object var_fObject){
		if(var_fObject instanceof ship){
			this.die();
			return false;
		}
		return true;
	}

	public void update(double d){
		super.update(d);
		if(time>lifeTime) this.die();
	}

	public int getSuppliesType(){
		return type;
	}

	public int getAmount(){
		return amounts[type];
	}
}
