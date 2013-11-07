package d3.game5;
import java.io.Serializable;
import d3.p3;
public abstract class weapon implements Serializable{
	public static final long serialVersionUID=1L;
	protected int ammo;
	protected double fire_tprv;
	protected double loadt;
	protected p3 rp;
	protected vehicle vh;
	protected double recoilf;
	protected boolean fireon;
	protected weapon(vehicle vh0,p3 rp0,double loadt0,int ammo0,final double recoilf0){
		loadt=loadt0;
		ammo=ammo0;
		vh=vh0;
		rp=rp0;
		recoilf=recoilf0;
	}
	public int ammo(){
		return ammo;
	}
	public boolean mayfire(){
		if(fire_tprv>0)
			return false;
		if(ammo<=0)
			return false;
		return true;
	}
	public boolean fire(boolean on){
		if(on){
			if(!mayfire())
				return false;
			ammo--;
			fire_tprv=loadt;
			if(fireon)
				return true;
			fireon=true;
			onfireon();
			return true;
		}else if(!fireon)
			return true;
		fireon=false;
		onfireoff();
		return true;
	}
	public String name(){
		return "";
	}
	private void onfireon(){
	}
	private void onfireoff(){
		vh.forces_set(getClass().getName(),null);				
	}
	public void update(double dt){
		if(fireon){
			final p3 p=vh.lookvec().scale(recoilf).negate();
			vh.forces_set(getClass().getName(),p);			
		}else
			vh.forces_set(getClass().getName(),null);
		if(fire_tprv==0)
			return;
		fire_tprv-=dt;
		if(fire_tprv<0)
			fire_tprv=0;
	}
}
