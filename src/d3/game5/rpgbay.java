package d3.game5;
import d3.p3;
public class rpgbay extends weapon{
	public static double loadtime=0.4;
	public static int nammo=1000000;
	public static final long serialVersionUID=1L;
	public rpgbay(vehicle host,p3 relPos){
		super(host,relPos,loadtime,nammo,0);
	}
	public boolean fire(final boolean on){
		if(super.fire(on)){
			p3 p=new p3();
			vh.to_wcs(rp,p);
			new rpg(vh,p);
			return true;
		}
		return false;
	}
}
