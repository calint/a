package d3.game5;
import d3.f3;
import d3.p3;
import d3.world;
public class recon extends vehicle{
	public static double accel=10;
	public static double brake=3;
	public static double climb=0;
	public static double decent=0;
	public static double health=1;
	public static double pitch=0;
	public static f3 polh=new f3("/d3/game4/recon.f3d");
	public static p3 scl=new p3(1.5,0.75,2.0);
	public static final long serialVersionUID=1L;
	public static double topspeed=30;
	public static double turn=Math.PI/4;
	public recon(world w,double x,double z,double a){
		super(w,new p3(x,scl.y,z),new p3(0,a,0),turn,pitch,accel,brake,topspeed,climb,decent,health,polh,scl);
		wpns.add(new blthgun(this,new p3(0,0,-scl.z)));
		wpns.add(new blthgun2(this,new p3(0,0,-scl.z)));
		wpns.add(new rpgbay(this,new p3(0,0,-scl.z)));
		do_wpnsel(1);
		hoverer=false;
		mass_kg=1000*70;
		forces_set("elevomaker",new p3(0,-mass_kg*w.gravity,0));
		//		hoverer=false;
	}
	public void death(){
		super.death();
		new reconcrp(this);
	}
}
