package d3.game;
import d3.f3;
import d3.p3;
import d3.world;
public class atg extends vehicle{
	static final long serialVersionUID=1L;
	public static f3 f3=new f3(atg.class);
	public static p3 scl=new p3(1,1,2);
	public static double hlth=20;
	public static double turn=1;
	public static double pitch=1;
	public static int fraggens=1;
	public static int nfrags=100;
	public static double fragrot=3;
	public static double fragspd=20;
	public static double fragsprd=2;
	public static double fragsz=3;
	public atg(world w,double x,double z,double a,ai ai){
		super(w,new p3(x,(scl.y+scl.z+scl.x),z),new p3(0,a,0),turn,pitch,0,0,0,0,0,hlth,f3,scl,true,ai);
		wpns.add(new blthgun(this,new p3(0,0,-scl.z)));
		wpns.add(new rpgbay(this,new p3(0,-scl.y-scl.y/2,0)));
		do_wpnsel(1);
		tracer=false;
	}
	public void death(){
		super.death();
		for(int n=0;n<nfrags;n++)
			new frag(world(),pos(),fragsz,fragsprd,fragspd,fragrot,fraggens);
	}
}
