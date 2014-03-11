package d3.game;
import d3.cfg;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
public class bullet extends objm{
	static final long serialVersionUID=1;
	public static double spread=0;
	protected double dmg;
	protected obj from;
	protected bullet(objm from,p3 p,double dmg,double velocity,f3 f3,p3 scl,double lftm,boolean hoverer,double zaxisrot){
		super(from.world(),p,from.agl(),from.dpos().add(from.lookvec().scale(velocity)).add(from.world().p3drnd(spread)).negate(),new p3(0,0,zaxisrot),f3,scl,type_bullet,1,lftm,hoverer);
		this.from=from;
		this.dmg=dmg;
		cfg.cnt_bullet++;
	}
	protected void on_hit(obj o){
		if(o==from)
			return;
		if(alive()){
			death();
			o.health(o.health()-dmg);
		}
		return;
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		if(groundhit){
			death();
			return;
		}
	}
}
