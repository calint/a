package d3.game;
import d3.p3;
public class rpgbay extends weapon{
	public static double loadtime=0.4;
	public static int nammo=1000000;
	public static final long serialVersionUID=1L;
	public rpgbay(vehicle host,p3 relPos){
		super(host,relPos,loadtime,nammo);
	}
	public boolean fire(){
		if(super.fire()){
			p3 p=new p3();
			o.to_wcs(rp,p);
			new rpg(o,p);
			return true;
		}
		return false;
	}
}
