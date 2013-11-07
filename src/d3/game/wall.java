package d3.game;
import d3.f3;
import d3.obj;
import d3.p3;
import d3.world;
public class wall extends obj{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(wall.class);
	public static double hlth=0;
	public wall(world wd,double x,double z,p3 a,double w,double b,double h){
		super(wd,new p3(x,h,z),a,type_struc,f3,new p3(w,h,b),hlth);
	}
}
