package d2.game;
/* fEnemyMissile - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Color;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;

class enemymissle extends enemyweapon{
	protected static polyh ourPolygonSolid;

	public enemymissle(world w,double x,double y,double agl,double vel,double lifetime){
		super(w,x,y,agl,vel*Math.cos(agl),vel*Math.sin(agl),0,lifetime);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(ourPolygonSolid));
		this.boundingradius(.8);
		dagl=15;
	}

	protected static void makePolygonSolid(){
		double[] vx={1,1,-1,-1};
		double[] vy={1,-1,-1,1};
		poly[]pl=new poly[1];
		int[]is={0,1,2,3};
		pl[0]=new poly(is,new Color(40,0,255));
		ourPolygonSolid=new polyh(vx,vy,pl);
		ourPolygonSolid.scalePoints(.6,.6);
	}
}
