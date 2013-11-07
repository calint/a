package d2.game;
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;
class shootingmine extends friendlyweapon{
	protected static polyh ourPolygonSolid;

	public shootingmine(world w,double x,double y,double vel,double agl){
		super(w,x,y,agl,vel*Math.cos(agl),vel*Math.sin(agl),2.0,3.0);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(ourPolygonSolid));
	}

	protected static void makePolygonSolid(){
		double[]vx={1.0,-1.0,-1.0,1.0};
		double[]vy={1.0,1.0,-1.0,-1.0};
		poly[]pl=new poly[1];
		pl[0]=new poly(new int[]{0,1,2,3},new Color(30,40,50));
		ourPolygonSolid=new polyh(vx,vy,pl);
	}

	public boolean interestedOfCollisionWith(object o){return false;}
	public void update(double dt){
		super.update(dt);
		wld.add(new missile(wld,x+Math.random()*2,y+Math.random()*2,15,Math.random()*Math.PI*2,3));
	}
}
