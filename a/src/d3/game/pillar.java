package d3.game;
import d3.f3;
import d3.obj;
import d3.p3;
import d3.world;
public class pillar extends obj{
	final static long serialVersionUID=1;
	public static f3 f3d=new f3(pillar.class);
	public static double health=1;
	public pillar(world world,double x,double z,p3 agl,double w,double b,double h){
		super(world,new p3(x,h,z),agl,type_struc,f3d,new p3(w,h,b),health);
	}
}
