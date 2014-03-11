package d3.game4;
import d3.p3;
public class blthgun extends weapon{
	public static int ammo0=1000000;
	public static double loadt0=1.0/14;
	public static final long serialVersionUID=1L;
	public blthgun(vehicle vh,p3 rp){
		super(vh,rp,loadt0,ammo0);
	}
	public boolean fire(){
		if(super.fire()){
			p3 p=new p3();
			vh.to_wcs(rp,p);
			new blth(vh,p);
			return true;
		}
		return false;
	}
}
