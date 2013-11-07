package d3.game5;
import d3.cfg;
import d3.f3;
import d3.p3;
public final class blth extends bullet{
	public static double dmg0=1;
	public static int fraggens=1;
	public static double fragrot=0;
	public static double fragsize=1.5;
	public static double fragspeed=0;
	public static double fragspread=1;
	public static int nfrags=1;
	public static f3 polh0=new f3("/d3/game4/blth.f3d");
	public static p3 scl0=new p3(0.13,0.13,2);
	private static final long serialVersionUID=1L;
	public static double spd0=111;
	public static double topage0=4;
	public static double trace_death_lftm=4;
	public static double trace_death_size_rnd_b=0.5;
	public static double trace_death_size_rnd_e=1.5;
	public static double trace_life_rnd_b=0;
	public static double trace_life_rnd_e=3;
	public static double trace_size_rnd_b=0.5;
	public static double trace_size_rnd_e=1.0;
	public static double zaxisrot0=Math.PI*5;
	public blth(vehicle h,p3 p,double spread){
		super(h,p,dmg0,spd0,polh0,scl0,topage0,true,zaxisrot0,spread);
		new blthshell(this);
		new trace(this,wld.rand(trace_size_rnd_b,trace_size_rnd_e),wld.rand(trace_life_rnd_b,trace_life_rnd_e));
		cfg.cnt_bullet++;
	}
	public void death(){
		super.death();
		new trace(this,wld.rand(trace_death_size_rnd_b,trace_death_size_rnd_e),trace_death_lftm);
		for(int n=0;n<nfrags;n++)
			new frag(world(),pos(),fragsize,fragspread,fragspeed,fragrot,fraggens);
	}
}
