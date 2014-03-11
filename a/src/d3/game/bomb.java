package d3.game;
import d3.f3;
import d3.objm;
import d3.p3;
import d3.world;
public final class bomb extends objm{
	static final long serialVersionUID=1L;
	public static f3 f3=new f3(bomb.class);
	public static p3 scl=new p3(0.5,0.5,1);
	public static int fraggens=1;
	public static double fragsize=1.4;
	public static double fragspeed=20;
	public static double fragspread=1;
	public static double health=1;
	public static double lftm=Double.MAX_VALUE;
	public static int nfrags=100;
	public static double randagl=1;
	public static double randrot=3;
	public static double dmg=35;
	public bomb(world w,p3 p,p3 a,p3 dp){
		super(w,p,a,new p3(0,0,0),w.p3drnd(randrot),f3,scl,type_scenery,health,lftm,false);
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		if(groundhit){
			new expl(world(),pos(),dmg*0.1,0.1,dmg,0.4,0.1);
			for(int n=0;n<nfrags;n++)
				new frag(world(),pos(),fragsize,fragspread,fragspeed,3,fraggens);
			death();
		}
	}
}
