package d3.game;
import d3.f3;
import d3.objm;
import d3.p3;
public final class blth extends bullet{
	static final long serialVersionUID=1L;
	public static f3 f3=new f3(blth.class);
	public static p3 scl=new p3(0.13,0.13,2);
	public static double dmg=1;
	public static int fraggens=1;
	public static double fragrot=0;
	public static double fragsize=1.5;
	public static double fragspeed=0;
	public static double fragspread=1;
	public static int nfrags=1;
	public static double spd=111;
	public static double lftm=4;
	public static double trace_death_lftm=4;
	public static double trace_death_size_rnd_b=0.5;
	public static double trace_death_size_rnd_e=1.5;
	public static double trace_life_rnd_b=0;
	public static double trace_life_rnd_e=3;
	public static double trace_size_rnd_b=0.5;
	public static double trace_size_rnd_e=1.0;
	public static double zaxisrot=Math.PI*5;
	public blth(objm src,p3 p){
		super(src,p,dmg,spd,f3,scl,lftm,true,zaxisrot);
		new blthshell(this);
		new trace(this,wld.rand(trace_size_rnd_b,trace_size_rnd_e),wld.rand(trace_life_rnd_b,trace_life_rnd_e));
	}
	public void death(){
		super.death();
		new trace(this,wld.rand(trace_death_size_rnd_b,trace_death_size_rnd_e),trace_death_lftm);
		for(int n=0;n<nfrags;n++)
			new frag(world(),pos(),fragsize,fragspread,fragspeed,fragrot,fraggens);
	}
}
