package d2.game;
/* fCruiser - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Color;
import d2.object;
import d2.poly;
import d2.polyh;
import d2.polyhi;
import d2.world;

class cruiser extends aobjectm{
	protected static polyh ourPolygonSolid;
	protected static int ourGun1=12;
	protected static int ourGun2=3;
	protected static int ourGun3;
	protected aobjectm theTarget;
	protected int myHealth;
	protected double myMaximumTurningAngle;

	public cruiser(world w,double x,double y,aobjectm trgo){
		super(w,x,y,0,0,0,0);
		theTarget=trgo;
		if(ourPolygonSolid==null) makePolygonSolid();
		polyhi(new polyhi(new polyh(ourPolygonSolid)));
		polyhi.scalePoints(2,2);
		boundingradius(6);
		myHealth=20;
		dangle(Math.random()*Math.PI*2);
		velocity(6);
		myMaximumTurningAngle=0.05;
	}

	public void update(double dt){
		super.update(dt);
		double a=getAngleToTarget();
		double da=a-angle();
		if(da>0.0)
			a=(da>myMaximumTurningAngle?agl+myMaximumTurningAngle:agl+da);
		else
			a=(-da>myMaximumTurningAngle?agl-myMaximumTurningAngle:agl+da);
		angle(a);
		angleandvelocity(a,(Math.PI-Math.abs(da))*4);
		if(Math.abs(da)<.5&&Math.random()<.1){
			if(Math.random()<.8){
				wld.add(new enemymissle(wld,polyhi.getXforPoint(ourGun1),polyhi.getYforPoint(ourGun1),angle(),1,2));
				wld.add(new enemymissle(wld,polyhi.getXforPoint(ourGun2),polyhi.getYforPoint(ourGun2),angle(),1,2));
			}else
				wld.add(new homingmissile(wld,polyhi.getXforPoint(ourGun3),polyhi.getYforPoint(ourGun3),angle(),35,.1,theTarget));
		}
	}

	protected void makePolygonSolid(){
		double[] vx={0.0,1.0,2.0,3.0,4.0,3.0,2.0,1.0,-1.0,-2.0,-3.0,-4.0,-3.0,-2.0,-1.0};
		double[] vy={4.0,0.0,1.0,3.0,1.0,-2.0,-2.0,-3.0,-3.0,-2.0,-2.0,1.0,3.0,1.0,0.0};
		poly[] pl=new poly[8];
		int[] is={1,0,14};
		pl[0]=new poly(is,new Color(120,20,20));
		int[] is_4_={1,14,8,7};
		pl[1]=new poly(is_4_,new Color(120,90,20));
		int[] is_5_={1,7,6,2};
		pl[2]=new poly(is_5_,new Color(0,90,20));
		int[] is_6_={4,2,6,5};
		pl[3]=new poly(is_6_,new Color(0,30,20));
		int[] is_7_={3,2,4};
		pl[4]=new poly(is_7_,new Color(80,10,20));
		int[] is_8_={14,13,9,8};
		pl[5]=new poly(is_8_,new Color(0,90,20));
		int[] is_9_={13,11,10,9};
		pl[6]=new poly(is_9_,new Color(0,30,20));
		int[] is_10_={13,12,11};
		pl[7]=new poly(is_10_,new Color(80,10,20));
		ourPolygonSolid=new polyh(vx,vy,pl);
		ourPolygonSolid.rotate(-1.5707963267948966);
	}

	public boolean interestedOfCollisionWith(object var_fObject){
		if(var_fObject instanceof weapon) return true;
		return false;
	}

	protected boolean handlecollision(object var_fObject){
		if(var_fObject instanceof weapon){
			myHealth--;
			if(myHealth<0){
				this.die();
				for(int i=0;i<10;i++){
					int i_11_=(int)(Math.random()*128.0);
					double d=Math.random()*2.0+1.0;
					wld.add(new fragment(wld,x+((Math.random()-0.5)*boundingrad),y+((Math.random()-0.5)*boundingrad),dx,dy,d*20.0,d,d*0.3,new Color(i_11_,i_11_,20)));
				}
				for(int i=0;i<5;i++){
					int i_12_=(int)(Math.random()*(double)(($)wld).getNbrSupplies());
					Color color=(($)wld).getSuppliesColor(i_12_);
					wld.add(new supplies(wld,x+((Math.random()-0.5)*boundingrad),y+((Math.random()-0.5)*boundingrad),Math.random()*5.0,Math.random()*3.141592653589793*2.0,i_12_,color));
				}
				return false;
			}
		}
		return true;
	}

	protected double getAngleToTarget(){
		double d=theTarget.x()-x;
		double d_13_=Math.atan((theTarget.y()-y)/d);
		if(d>0.0) return d_13_;
		return d_13_+3.141592653589793;
	}
}
