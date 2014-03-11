package d2.game;
import java.awt.Color;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.object;
import d2.world;

class asteroid extends aobjectm{
	protected static int asteroidsAlive;
	protected double size;

	public static void reset(){
		asteroidsAlive=0;
	}

	public static int getAsteroidsAlive(){
		return asteroidsAlive;
	}

	public asteroid(world w,double x,double y,double a,double dx,double dy,double dz,double da){
		super(w,y,a,dx,dy,dz,da);
		size=x;
		this.boundingradius(size);
		this.polyhi(new polyhi(returnPolygonSolid()));
		asteroidsAlive++;
	}

	protected polyh returnPolygonSolid(){
		int i=(int)(size/2.0);
		i=i<3?3:i;
		double[] vx=new double[i+1];
		double[] vy=new double[i+1];
		double d=6.283185307179586/(double)i;
		double a=0.0;
		for(int i_8_=0;i_8_<i;i_8_++){
			vx[i_8_+1]=size*(Math.cos(a)+Math.random()*0.6-0.3);
			vy[i_8_+1]=size*(Math.sin(a)+Math.random()*0.6-0.3);
			a+=d;
		}
		vx[0]=Math.random()*size*0.3;
		vy[0]=Math.random()*size*0.3;
		poly[] polys=new poly[i];
		for(int i_9_=0;i_9_<i-1;i_9_++){
			int[] is=new int[3];
			is[0]=0;
			is[1]=i_9_+1;
			is[2]=i_9_+2;
			int i_10_=(int)(Math.random()*128.0);
			polys[i_9_]=new poly(is,new Color(i_10_,i_10_,i_10_));
		}
		int[] is=new int[3];
		is[0]=0;
		is[1]=i;
		is[2]=1;
		int i_11_=(int)(Math.random()*128.0);
		polys[i-1]=new poly(is,new Color(i_11_,i_11_,i_11_));
		return new polyh(vx,vy,polys);
	}

	public boolean interestedOfCollisionWith(object o){
		if(o instanceof weapon) return true;
		if(o instanceof enemyweapon) return true;
		if(o instanceof ship) return true;
		if(size>3.0&&o instanceof cruiser) return true;
		return false;
	}

	protected boolean handlecollision(object o){
		if(size>3.0){
			double d=size*0.5;
			for(int i=0;i<3;i++)
				wld.add(new asteroid(wld,d,x+(Math.random()-0.5)*2.0*size,y+(Math.random()-0.5)*2.0*size,agl,dx+Math.random()*dy-0.5,dy+Math.random()*dx-0.5,dagl*(1.0+Math.random())));
		}
		this.die();
		asteroidsAlive--;
		for(int i=0;i<(int)size;i++){
			int i_12_=(int)(Math.random()*64.0);
			wld.add(new fragment(wld,x+(Math.random()-0.5)*size*2.0,y+(Math.random()-0.5)*size*2.0,dx,dy,40.0,size*0.3,Math.random()*0.5+0.4,new Color(i_12_,i_12_,i_12_)));
		}
		return false;
	}
}
