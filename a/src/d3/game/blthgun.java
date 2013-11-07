package d3.game;
import d3.p3;
public class blthgun extends weapon{
	static final long serialVersionUID=1L;
	public static int ammo0=1000000;
	public static double loadt0=1.0/14;
	public blthgun(vehicle vh,p3 rp){
		super(vh,rp,loadt0,ammo0);
	}
	public boolean fire(){
		if(super.fire()){
			p3 p=new p3();
			o.to_wcs(rp,p);
			new blth(o,p);
			return true;
		}
		return false;
	}
}
