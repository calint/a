package d3.game;
import d3.cfg;
import d3.f3;
import d3.objm;
import d3.p3;
public final class rpg extends bullet{
	final static long serialVersionUID=1;
	public static f3 f3d=new f3(rpg.class);
	public static p3 scl=new p3(0.3,0.3,0.3);
	public static int lftm=22;
	public static double dmg=100;
	public static double expl_scl0=5;
	public static double expl_scl1=15;
	public static double expl_scl2=8;
	public static double expl_t0=1;
	public static double expl_t1=0.3;
	public static int frag_cnt=100;
	public static double frag_t0sprd=expl_scl2;
	public static int fraggns=1;
	public static double fragrot=3;
	public static double fragspd=21;
	public static double fragsz=1.2;
	public static double topspd0=111;
	public static double trace_evry=0.1;
	public static double trace_lftm=0.6;
	public static double trace_size=1;
	public static double zaxisrot0=Math.PI;
	private double trace_t=trace_evry;
	public rpg(objm shooter,p3 p){
		super(shooter,p,dmg,topspd0,f3d,scl,lftm,false,zaxisrot0);
		cfg.cnt_rpg++;
	}
	public void death(){
		super.death();
		new expl(wld,pos(),expl_scl0,expl_t0,expl_scl1,expl_t1,expl_scl2);
		$.mkfrags(this,frag_cnt,fragsz,frag_t0sprd,fragspd,fragrot,fraggns);
	}
	public void upd_dt(final double dt){
		super.upd_dt(dt);
		trace_t+=dt;
		if(trace_t>trace_evry){
			new trace(this,trace_size,trace_lftm);
			trace_t-=trace_evry;
		}
	}
}
