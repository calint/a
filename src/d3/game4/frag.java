package d3.game4;
import d3.cfg;
import d3.f3;
import d3.objm;
import d3.p3;
import d3.world;
public final class frag extends objm{
	public static double bounce=0.5;
	public static f3 polyh=new f3("/d3/game4/frag.f3d");
	public static int refragn=2;
	public static double refragresize=0.5;
	public static double refragrot=3;
	private static final long serialVersionUID=1L;
	public static int topage=22;
	public static double vely=3;
	public int gen;
	double k0=0.5;
	double k1=1;
	public double size;
	public double speed;
	public frag(world w,p3 p,double size0,double spread,double speed0,double rotation,int gen0){
		super(w,w.p3drnd(p,spread),w.p3drnd(0.0,3.0),w.p3drnd(speed0),w.p3drnd(0.0,3.0),polyh,w.p3drnd(0.0,size0),type_scenery,1,topage,true);
		gen=gen0-1;
		speed=speed0;
		size=size0;
		cfg.cnt_frag++;
	}
	public void refrag(){
		if(gen>0){
			for(int n=0;n<refragn;n++){
				new frag(world(),pos(),size*refragresize,size,dpos().y,refragrot,gen-1);
			}
		}
		death();
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		p3 dp=dpos();
		dp.y-=wld.gravity*dt*k1;
		p3 p=pos();
		if(p.y<0){
			p.y=-p.y*0.5;
			dp.y=-dp.y*k0;
			pos(p);
		}
		dpos(dp);
	}
}
