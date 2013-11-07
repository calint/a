package d3.game5;
import d3.f3;
import d3.obj;
import d3.p3;
public class cube extends obj{
	private static f3 polh=new f3("/d3/game4/cube.f3d");
	private static final long serialVersionUID=1L;
	public cube(d3.world w,p3 p,p3 s){
		super(w,p,new p3(),type_struc,polh,s,8);
	}
}
