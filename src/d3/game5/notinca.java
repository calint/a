package d3.game5;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
import d3.world;
public class notinca extends objm{
	public static f3 polyh=new f3('/'+notinca.class.getName().replace('.','/')+".f3d");
	private static final long serialVersionUID=1L;
	public notinca(world w,p3 p){
		super(w,p,new p3(),new p3(),new p3(),polyh,new p3(0.25),obj.type_strucm,Double.MAX_VALUE,Double.MAX_VALUE,true);
	}
}
