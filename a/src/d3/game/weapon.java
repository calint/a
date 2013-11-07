package d3.game;
import java.io.Serializable;
import d3.objm;
import d3.p3;
public abstract class weapon implements Serializable{
	final static long serialVersionUID=1;
	protected int ammo;
	protected double fire_tprv;
	protected double loadt;
	protected p3 rp;
	protected objm o;
	protected weapon(objm o,p3 rp0,double loadt0,int ammo0){
		loadt=loadt0;
		ammo=ammo0;
		this.o=o;
		rp=rp0;
	}
	public int ammo(){
		return ammo;
	}
	public boolean fire(){
		if(fire_tprv>0)
			return false;
		if(ammo<=0)
			return false;
		ammo--;
		fire_tprv=loadt;
		return true;
	}
	public String name(){
		return "";
	}
	public void update(double dt){
		if(fire_tprv==0)
			return;
		fire_tprv-=dt;
		if(fire_tprv<0)
			fire_tprv=0;
	}
}
