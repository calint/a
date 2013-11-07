package d3.game;
import d3.f3;
import d3.p3;
public final class blthshell extends bulletshell{
	static final long serialVersionUID=1L;
	public static f3 f3=new f3(blthshell.class);
	public static p3 scl=new p3(0.1,0.1,0.2);
	public static double lftm=21;
	public static double rndaglx=2;
	public static double rndagly=2;
	public static double rndaglz=2;
	public static double rndrot=2;
	public static double rndsprd=2;
	public static double spd=4.0;
	public static double spdbk=0.1;
	public blthshell(blth o){
		super(o.world(),o.to_wcs(new p3(0,0,blth.scl.z+scl.z)),o.agl(),o.dpos().negate().scale(spdbk),new p3(rndaglx,rndagly,rndaglz),rndsprd,rndrot,lftm,f3,scl);
	}
}
