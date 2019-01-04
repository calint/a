package d2.game;
/* fShip - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Color;
import java.awt.Graphics;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.viewport;
import d2.world;

class ship extends aobjectm{
	public static int THRUST;
	public static int MISSILE=1;
	public static int BACK=2;
	public static int SHOTGUN=3;
	public static int CLUSTER=4;
	public static int SPINING=5;
	public static int SHOOTING=6;
	public static int BACKTHRUST=7;
	public static int SHIELD=8;
	public static int CONTROLLED=9;
	public static int ENERGI=10;
	protected poly myThrustPolygon;
	protected static polyh ourPolygonSolid;
	final public double[]mySupplies=new double[]{100,50,20,10,5,5,3,20,3,2,100};
	public int myScore;
	protected double myShieldTimeLeft;

	public ship(world w,double x,double y,double a,double dx,double dy,double da){
		super(w,x,y,a,dx,dy,da);
		if(ourPolygonSolid==null)makePolygonSolid();
		boundingradius(4);
		polyhi(new polyhi(new polyh(ourPolygonSolid)));
		myThrustPolygon=polyhi.polyh().polygons()[2];
	}

	protected static void makePolygonSolid(){
		double[]x={5,-2.3, -2.3,-1};
		double[]y={0,4.45,-4.45, 0};
		poly[]pg=new poly[3];
		int[]pix1={0,1,3};
		pg[0]=new poly(pix1,new Color(0,128,0));
		int[]pix2={0,3,2};
		pg[1]=new poly(pix2,new Color(0,64,0));
		int[]pix3={3,1,2};
		pg[2]=new poly(pix3,Color.black);
		ourPolygonSolid=new polyh(x,y,pg);
	}

	public void turn(double da,double dt){dagl=da;}
	public void thrust(double s,double dt){
		if(s>0.0&&mySupplies[THRUST]>0||s<0.0&&mySupplies[BACKTHRUST]>0){
			dx+=s*Math.cos(agl);
			dy+=s*Math.sin(agl);
			myThrustPolygon.color(Color.red);
			mySupplies[s>0.0?THRUST:BACKTHRUST]-=dt;
		}
	}
	public void noThrust(){myThrustPolygon.color(Color.black);}

	private final double weapon_fire_rate_s[]=new double[]{1,.1,1,.25,1,1,1,1,1};
	private final double weapon_last_fire_s[]=new double[9];
	
	public void shoot(int i,double dt){
		if(mySupplies[i]<=0)
			return;
		mySupplies[i]-=dt;
		final double ds=time-weapon_last_fire_s[i];
		if(ds<weapon_fire_rate_s[i])
			return;
		weapon_last_fire_s[i]=time;
		switch(i){
		case 1:wld.add(new missile(wld,x,y,50,agl,2.0));break;
		case 3:wld.add(new shotgunmissile(wld,x,y,80,agl,5,0.39269908169872414));break;
		case 4:wld.add(new clustermissile(wld,x,y,20,Math.PI+agl,20));break;
		case 5:wld.add(new spinningmine(wld,x,y,5,Math.PI+agl));break;
		case 6:wld.add(new shootingmine(wld,x,y,5,Math.PI+agl));break;
		case 2:wld.add(new missile(wld,x,y,50,Math.PI+agl,2));break;
		case 8:myShieldTimeLeft=3.0;break;
		case 9:wld.add(new controlledcluster(wld,x,y,20,agl,20,this));break;
		}
	}

	public void update(double dt){
		super.update(dt);
		if(myShieldTimeLeft>0.0) myShieldTimeLeft-=dt;
		if(mySupplies[ENERGI]<0){
			for(int i=0;i<10;i++)
				wld.add(new fragment(wld,x,y,dx,dy,40.0,Math.random()*3.0,4.0,new Color((int)Math.random()*255,50,30)));
			this.die();
		}
	}

	public void paint(Graphics g,viewport vp){
		super.paint(g,vp);
		if(myShieldTimeLeft>0.0){
			int i=(int)(myShieldTimeLeft*80.0);
			g.setColor(new Color(i,0,0));
			int j=(int)vp.scaleX(boundingrad+myShieldTimeLeft*.5);
			g.drawOval(vp.x(x)-j,vp.y(y)-j,j<<1,j<<1);
		}
	}

	public boolean interestedOfCollisionWith(object o){
		if(o instanceof asteroid) return true;
		if(o instanceof fragment) return true;
		if(o instanceof supplies) return true;
		if(o instanceof enemyweapon) return true;
		if(o instanceof cruiser) return true;
		return false;
	}

	protected boolean handlecollision(object o){
		if(myShieldTimeLeft<=0.0){
			if(o instanceof asteroid||o instanceof cruiser||o instanceof fragment){
				double d=o.boundingradius();
				angle(angle()+(Math.random()-0.5)*d);
				mySupplies[ENERGI]-=d*20.0;
				return true;
			}
			if(o instanceof enemyweapon){
				mySupplies[ENERGI]=-1;
				return false;
			}
		}
		if(o instanceof supplies){
			supplies s=(supplies)o;
			mySupplies[s.getSuppliesType()]+=s.getAmount();
			return true;
		}
		return true;
	}
}
