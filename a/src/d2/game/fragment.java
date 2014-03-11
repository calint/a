package d2.game;
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;

class fragment extends aobjectm{
	protected static polyh ourPolygonSolid;
	protected double myLife;
	protected double size;
	protected Color myColor;

	public fragment(world w,double x,double y,double dx,double dy,double explforce,double size0,double lifetime,Color color){
		super(w,x,y,Math.random()*Math.PI*2.0, dx+(Math.random()-0.5)*explforce,dy+(Math.random()-0.5)*explforce,Math.random()*2.0);
		myLife=lifetime;
		size=size0;
		myColor=color;
		this.boundingradius(size);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(new polyh(ourPolygonSolid)));
		polyhi.scalePoints(size,size);
		polyhi.distortPoints(size*0.4,size*0.4);
		polyhi.setPolygonColor(0,color);
	}

	protected static void makePolygonSolid(){
		double[]vx={1.0,-0.87,-0.87};
		double[]vy={0.0,0.5,-0.5};
		poly[]pl=new poly[1];
		int[]is={0,1,2};
		pl[0]=new poly(is,Color.black);
		ourPolygonSolid=new polyh(vx,vy,pl);
	}

	public void update(double d){
		super.update(d);
		if(time>myLife) this.die();
	}

	public boolean interestedOfCollisionWith(object var_fObject){
		if(var_fObject instanceof ship) return true;
		return false;
	}

	protected boolean handlecollision(object var_fObject){
		this.die();
		return false;
	}
}
