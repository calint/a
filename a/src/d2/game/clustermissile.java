package d2.game;
/* fClusterMissile - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;
class clustermissile extends friendlyweapon{
	protected static polyh ourPolygonSolid;
	protected int nbr;
	protected double myExplodingTime;
	
	public clustermissile(world w,double x,double y,double vel,double agl,int n){
		super(w,x,y,agl,vel*Math.cos(agl),vel*Math.sin(agl),0,.8);
		nbr=n;
		myExplodingTime=.7;
		initiate();
	}

	public clustermissile(world w,double x,double y,double vel,double agl,int i,double n,double lifetime){
		super(w,x,y,agl,vel*Math.cos(agl),vel*Math.sin(agl),0,lifetime);
		nbr=i;
		myExplodingTime=lifetime;
		initiate();
	}

	protected void initiate(){
		this.boundingradius(1.0);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(ourPolygonSolid));
	}

	protected static void makePolygonSolid(){
		final double[]vx={ 1,-1,-1, 1};
		final double[]vy={ 1, 1,-1,-1};
		final int[]ix={0,1,2,3};
		final poly[]polys=new poly[1];
		polys[0]=new poly(ix,Color.blue);
		ourPolygonSolid=new polyh(vx,vy,polys);
	}

	public void update(double d){
		if(time>myExplodingTime) explode();
		super.update(d);
	}

	protected void explode(){
		for(int i=0;i<nbr;i++){
			double d=6.283185307179586*Math.random();
			double d_9_=20.0+10.0*Math.random();
			wld.add(new missile(wld,x+Math.random()*5.0,y+Math.random()*5.0,d_9_,d,1.0));
		}
		this.die();
	}

	protected boolean handlecollision(object var_fObject){
		if(var_fObject instanceof asteroid||var_fObject instanceof cruiser){
			explode();
			return false;
		}
		return true;
	}
}
