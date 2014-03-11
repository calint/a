package d3.game4;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
public class bullet extends objm{
	private static final long serialVersionUID=1L;
	protected static double spread=0;
	protected double dmg;
	protected obj shooter;
	protected bullet(objm shooter0,p3 p,double dmg0,double velocity0,f3 ph0,p3 s0,double lifetime0,boolean hoverer0,double zaxisrot0){
		super(shooter0.world(),p,shooter0.agl(),shooter0.dpos().add(shooter0.lookvec().scale(velocity0)).add(shooter0.world().p3drnd(spread)).negate(),new p3(0,0,zaxisrot0),ph0,s0,type_bullet,1,lifetime0,hoverer0);
		shooter=shooter0;
		dmg=dmg0;
	}
	public double damage(){
		return dmg;
	}
	protected void on_hit(obj obj){
		if(alive()){
			death();
			obj.health(obj.health()-dmg);
		}
	}
	public obj shooter(){
		return shooter;
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		if(groundhit){
			death();
			return;
		}
	}
}
