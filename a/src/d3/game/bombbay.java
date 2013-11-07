package d3.game;
import d3.p3;
public class bombbay extends weapon{
	final static long serialVersionUID=1;
	public static int defaultammo=1000000;
	public static double loadingtime=1;
	public bombbay(vehicle vh,p3 rp){
		super(vh,rp,loadingtime,defaultammo);
	}
	public boolean fire(){
		if(super.fire()){
			p3 p=new p3();
			o.to_wcs(rp,p);
			new bomb(o.world(),p,o.agl(),o.dpos());
			return true;
		}
		return false;
	}
}
