package d3;

import java.util.HashMap;
import java.util.Map;

public abstract class objm extends obj{
	private static final long serialVersionUID=1L;
	private double age;
	private p3 aglprv;
	private p3 dagl;
	private p3 ddpos;
	private p3 dpos;
	protected boolean groundhit;
	protected boolean hoverer;
	private double lftm;
	private double pbouncevel=0.5;
	private double pbouncey=0.9;
	private p3 posprv;
	public objm(world w,p3 p,p3 a,p3 dp,p3 da,f3 f3,p3 scl,int type,double hlth,double lftm,boolean hoverer){
		super(w,p,a,type,f3,scl,hlth);
		this.lftm=lftm;
		dpos=dp;
		dagl=da;
		ddpos=new p3();
		this.hoverer=hoverer;
	}
	public final double age(){
		return age;
	}
	public final p3 dagl(){
		return dagl.clone();
	}
	public final void dagl(final p3 a){
		dagl.set(a);
	}
	public final p3 dpos(){
		return dpos.clone();
	}
	public final void dpos(final p3 p){
		dpos.set(p);
	}
	protected final void restore_prev_state(){
		pos(posprv);
		agl(aglprv);
	}
	private Map<String,p3>forces=new HashMap<String,p3>();
	public double mass_kg;
	public void forces_set(final String name,final p3 v){
		if(v==null)
			forces.remove(name);
		else
			forces.put(name,v);
	}
	public void upd_dt(final double dt){
		super.upd_dt(dt);
		if(enable_forcess&&mass_kg>0){
			System.out.println("-------------------");
			final p3 f=new p3();
			for(p3 i:forces.values()){
				System.out.println(i);
				f.add(i);
			}
			f.scale(1/mass_kg);
//			System.out.println(this+" fg="+f);
			ddpos.set(f);
		}
		age+=dt;
		if(age>lftm){
			death();
			return;
		}
		posprv=pos();
		aglprv=agl();
		dpos.add(ddpos,dt);
		if(!hoverer)
			dpos.y+=wld.gravity*dt;
		pos.add(dpos,dt);
		if(pos.y<scl.y){
			groundhit=true;
			pos.y=scl.y;
			dpos.y=-dpos.y*pbouncey;
			dpos.scale(pbouncevel);
		}
		agl.add(dagl,dt);
		if(!posprv.equals(pos))
			upd_mw=upd_box=upd_pw=upd_grds=true;
		if(!aglprv.equals(agl))
			upd_mw=upd_box=upd_pw=upd_nmls=upd_itn=true;
	}
}
