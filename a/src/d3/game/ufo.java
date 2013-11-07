package d3.game;
import d3.f3;
import d3.p3;
import d3.world;
public class ufo extends vehicle{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(ufo.class);
	public static p3 scl=new p3(6,1,4);
	public static double accel=30;
	public static double brakes=2;
	public static double climb=10;
	public static double decent=20;
	public static double health=2000;
	public static double pitch=0.8;
	public static double topspeed=50;
	public static double turn=0.6;
	public ufo(world w,double x,double z,double y,double trn,double ptch,ai ai){
		super(w,new p3(x,scl.y+y,z),new p3(ptch,trn,0),turn,pitch,accel,brakes,topspeed,climb,decent,health,f3,scl,true,ai);
		wpns.add(new blthgun(this,new p3(0,-scl.y-blth.scl.y*2,-scl.z/16-scl.z/4)));
		wpns.add(new rpgbay(this,new p3(0,-scl.y/2,-scl.z/2)));
		wpns.add(new bombbay(this,new p3(0,-scl.y,0)));
		do_wpnsel(2);
	}
	public void death(){
		super.death();
		new ufocrp(this);
	}
}
