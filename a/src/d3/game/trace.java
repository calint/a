package d3.game;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
public final class trace extends objm{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(trace.class);
	private double k=0.5;
	public trace(obj osrc,double size0,double lifetime0){
		super(osrc.world(),osrc.pos(),osrc.agl(),new p3(),new p3(-5,-5,-5),f3,new p3(size0),type_scenery,1,lifetime0,true);
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		scale(scale().scale(1.0-k*dt));
		p3 dp=dpos();
		dp.scale(k*dt);
	}
}
