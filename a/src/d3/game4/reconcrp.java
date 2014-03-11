package d3.game4;
import d3.f3;
import d3.objm;
import d3.p3;
public final class reconcrp extends objm{
	public static int frag_count=100;
	public static int frag_gens=1;
	public static double frag_rot=3;
	public static double frag_size=1.2;
	public static double frag_speed=10;
	public static double frag_spread=0.1;
	public static f3 polh=new f3("/d3/game4/reconcrp.f3d");
	public static p3 scl=new p3(1.5,0.23,2);
	private static final long serialVersionUID=1L;
	public static double topage=Double.MAX_VALUE;
	public static double upspeed=10;
	public reconcrp(recon o){
		super(o.world(),o.pos(),o.agl(),o.dpos().y(o.world().rand(0,upspeed)),o.world().p3drnd(frag_rot),polh,scl,type_scenery,1,topage,true);
		new expl(world(),pos(),5,0.3,16,0.6,4);
		for(int n=0;n<frag_count;n++)
			new frag(this.world(),this.pos(),frag_size,frag_spread,frag_speed,frag_rot,frag_gens);
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		p3 p=pos();
		p3 dp=dpos();
		if(p.y<scl.y){
			dp.scale(0.5);
			dp.y=-dp.y;
			p.y=0;
			pos(p);
			dagl(dagl().negate().scale(0.5));
			dpos(dp);
		}
		if(dp.y==0)
			dagl(dagl().x(0.0).z(0.0));
	}
}
