package d3.game4;
import java.io.Serializable;
import d3.p3;
public abstract class weapon implements Serializable{
	public static final long serialVersionUID=1L;
	protected int ammo;
	protected double fire_tprv;
	protected double loadt;
	protected p3 rp;
	protected vehicle vh;
	protected weapon(vehicle vh0,p3 rp0,double loadt0,int ammo0){
		loadt=loadt0;
		ammo=ammo0;
		vh=vh0;
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
