package d3.game4;
import d3.f3;
import d3.p3;
public final class blthshell extends bulletshell{
	protected static double lftm0=21;
	protected static f3 polh0=new f3("/d3/game4/blthshell.f3d");
	protected static double rndaglx0=2;
	protected static double rndagly0=2;
	protected static double rndaglz0=2;
	protected static double rndrot0=2;
	protected static double rndsprd0=2;
	protected static p3 scl0=new p3(0.1,0.1,0.2);
	private static final long serialVersionUID=1L;
	protected static double spd0=4.0;
	protected static double spdbk0=0.1;
	public blthshell(blth o){
		super(o.world(),o.to_wcs(new p3(0,0,blth.scl0.z+scl0.z)),o.agl(),o.dpos().negate().scale(spdbk0),new p3(rndaglx0,rndagly0,rndaglz0),rndsprd0,rndrot0,lftm0,polh0,scl0);
	}
}
