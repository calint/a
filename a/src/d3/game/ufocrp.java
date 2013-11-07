package d3.game;
import d3.f3;
import d3.objm;
import d3.p3;
public final class ufocrp extends objm{
	final static long serialVersionUID=1;
	public static f3 f3=new f3(ufocrp.class);
	public static p3 scl=new p3(8,0.2,5);
	public static double lftm=Double.MAX_VALUE;
	public static double hlth=1;
	public static int fraggens=2;
	public static double fragsize=1.5;
	public static double fragspeed=14;
	public static double fragspread=0.1;
	public static int nfrags=155;
	public static double rotrand=0.2;
	public ufocrp(ufo o){
		super(o.world(),o.pos(),o.agl(),o.dpos(),o.world().p3drnd(rotrand),f3,scl,type_scenery,hlth,lftm,true);
		for(int n=0;n<nfrags;n++){
			new frag(world(),pos(),fragsize,fragspread,fragspeed,3,fraggens);
		}
	}
}
