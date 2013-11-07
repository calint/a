package d2.game;
import java.awt.Color;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;
class missile extends friendlyweapon{
	protected static polyh polyh;

	public missile(world w,double x,double y,double v,double a,double lifetime){
		super(w,x,y,a,v*Math.cos(a),v*Math.sin(a),0,lifetime);
		if(polyh==null) mkpolyh();
		this.boundingradius(1.8);
		this.polyhi(new polyhi(polyh));
	}

	protected static void mkpolyh(){
		double[]vx={1,-.87,-.87};
		double[]vy={0,.5,-.5};
		int[]ix={0,1,2};
		poly[] pl=new poly[1];
		pl[0]=new poly(ix,Color.red);
		polyh=new polyh(vx,vy,pl);
	}
}
