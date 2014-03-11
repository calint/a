package d3.game;
import d3.f3;
import d3.p3;
import d3.world;
public class recon extends vehicle{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(recon.class);
	public static p3 scl=new p3(1.5,0.75,2.0);
	public static double hlth=1;
	public static double turn=1.5;
	public static double accel=10;
	public static double topspeed=30;
	public static double brake=3;
	public static double pitch=0;
	public static double climb=0;
	public static double decent=0;
	public recon(world w,double x,double z,double a,ai ai){
		super(w,new p3(x,scl.y,z),new p3(0,a,0),turn,pitch,accel,brake,topspeed,climb,decent,hlth,f3,scl,true,ai);
		wpns.add(new blthgun(this,new p3(0,0,-scl.z)));
		wpns.add(new rpgbay(this,new p3(0,0,-scl.z)));
		do_wpnsel(1);
	}
	public void death(){
		super.death();
		new reconcrp(this);
	}
}
