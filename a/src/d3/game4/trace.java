package d3.game4;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
public final class trace extends objm{
	public static f3 polyh=new f3("/d3/game4/trace.f3d");
	private static final long serialVersionUID=1L;
	double k0=0.5;
	public trace(obj osrc,double size0,double lifetime0){
		super(osrc.world(),osrc.pos(),osrc.agl(),new p3(),new p3(-5,-5,-5),polyh,new p3(size0),type_scenery,1,lifetime0,true);
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		scale(scale().scale(1.0-k0*dt));
		p3 dp=dpos();
		dp.scale(k0*dt);
	}
}
