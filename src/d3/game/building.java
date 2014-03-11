package d3.game;
import d3.f3;
import d3.obj;
import d3.p3;
import d3.world;
public final class building extends obj{
	final static long serialVersionUID=1L;
	public static f3 f3=new f3(building.class);
	public static double health=1;
	public building(world wld,double x,double z,double w,double b,double h){
		super(wld,new p3(x,h,z),new p3(),type_struc,f3,new p3(w,h,b),health);
	}
	public void death(){
		super.death();
		new buildingcrp(this);
	}
}
