package d3.game;
import d3.f3;
import d3.objm;
import d3.p3;
import d3.world;
public class screen extends objm{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(screen.class);
	public static p3 scl=new p3(1);
	public screen(world w,p3 p,p3 a){
		super(w,p,a,new p3(),new p3(),f3,scl,type_scenery,0,Double.MAX_VALUE,true);
	}
}
