package d3.game;
import java.util.LinkedList;
import java.util.List;
import d3.applet;
import d3.cfg;
import d3.m3;
import d3.obj;
import d3.objm;
import d3.p3;
public final class ai{
//	public String toString(){return state+"  "+applet.num((long)(10*turn_dotprod),2)+"  "+applet.num((long)(10*pitch_dotprod),2);}
	public String toString(){return status();}
	
	
	static final int ADJUSTING_HEIGHT=3;
	static final int ATTACK=2;
	static final int FIRE_INTERSECT=4;
	static final int MOVING=1;
	static final int STANDBY=0;
	private double aquiretarget_evry=0.5;
	private double aquiretarget_t;
	private double atkrng=256;
	private double atkrng2=atkrng*atkrng;
	private double attack_pitch_err_margin=2.0;
	private double attack_turn_err_margin=2.0;
	private double margin_height=1;
	private m3 mwv;
	private double pitch_dotprod;
	private int state;
	private int trgbits=obj.type_vehicle;
	private obj trg;
	private double tg_dist;
	private double turn_dotprod;
	private waypnt waypoint;
	private vehicle vhl;
	public vehicle vhl(){return vhl;}
	public ai vhl(final vehicle vhl){this.vhl=vhl;return this;}
	public ai trgbits(final int trgbits){this.trgbits=trgbits;return this;}
	public ai trg(final objm trg){this.trg=trg;state=ATTACK;return this;}
	private String distm(final double a){return ""+(int)Math.floor(a);}
	public ai go(final p3 p){
		final double error=1;
		waypoint=new waypnt(p,error);
		state=MOVING;
		return this;
	}
	private double turn_towards(final p3 p){
		final p3 v1=mwv.axis_x();
		final p3 v2=new p3(vhl.pos(),p);
		turn_dotprod=v1.dotprod(v2);
		if(turn_dotprod<0){
			if(turn_dotprod<-1)vhl.btn_turn(1.0);
			else vhl.btn_turn(turn_dotprod);
		}else if(turn_dotprod>0){
			if(turn_dotprod>1)vhl.btn_turn(-1.0);
			else vhl.btn_turn(turn_dotprod);
		}else{
			vhl.btn_turn(0.0);
		}
		return turn_dotprod;
	}
	private double pitch_towards(final p3 p){
		final p3 v1=mwv.axis_y();
		final p3 v2=new p3(vhl.pos(),p);
		pitch_dotprod=v1.dotprod(v2);
		if(pitch_dotprod>0){vhl.btn_pitch(-1.0);}
		else if(pitch_dotprod<0){vhl.btn_pitch(1.0);}
		else{vhl.btn_pitch(0.0);}
		return pitch_dotprod;
	}
	private double move_towards(final p3 p){
		final p3 v1=new p3(vhl.pos(),p);
		final p3 v2=vhl.axis_z();
		final double s=v1.dotprod(v2);
		if(s>0){vhl.btn_gas(0.0);vhl.btn_brake(1.0);}
		else if(s<0){vhl.btn_gas(1.0);vhl.btn_brake(0.0);}
		else{vhl.btn_brake(1.0);vhl.btn_gas(0.0);}
		return s;
	}
	private boolean climb_towards(final p3 p){
		final double error=p.y-vhl.pos().y;
		final double diff=vhl.flev-error;
		final double rate=diff/vhl.flev;
		if(error>margin_height){vhl.btn_decent(0);vhl.btn_climb(Math.min(Math.abs(rate),1));}
		else if(error<-margin_height){vhl.btn_decent(Math.min(Math.abs(rate),1));vhl.btn_climb(0);}
		else{vhl.btn_climb(0);vhl.btn_decent(0);return true;}
		return false;
	}
	public String status(){
		switch(state){
		case STANDBY:return "standby";
		case MOVING:return "moving to "+waypoint+"  "+vhl.pos();
		case ATTACK:return "attacking "+trg.getClass().getName()+"@"+Integer.toHexString(trg.hashCode())+"  "+distm(tg_dist)+"m  terr:"+applet.num((long)(turn_dotprod*100),2)+"  perr:"+applet.num((long)(pitch_dotprod*100),2);
		default:return "?";
		}
	}
	public void update(final double dt){
		if(state==STANDBY){
			vhl.btn_turn(0);
			vhl.btn_pitch(0);
			vhl.btn_gas(0);
			vhl.btn_brake(1);
			aquiretarget_t+=dt;
			if(aquiretarget_t>aquiretarget_evry){
				aquiretarget_t=0;
				final List<obj>ls=new LinkedList<obj>();
				vhl.world().q_rng(trgbits,vhl.pos(),atkrng,ls);
				obj nxtg=null;
				double distz=0;
				for(final obj o:ls){
					if(!o.alive())continue;
					if(o==vhl)continue;
					if(vhl.getClass().equals(o.getClass()))continue;
					final p3 v=new p3(vhl.pos(),o.pos()).norm();
					final double d=v.dotprod(vhl.lookvec());
					if(d<0)continue;
					final double range2=o.pos().distance2_to(vhl.pos());
					if(distz<d&&range2<atkrng2){
						distz=d;
						nxtg=o;
					}
				}
				if(nxtg!=null){
					trg=nxtg;
					if(trg instanceof vehicle&&cfg.focus_target)vhl.world().player((vehicle)trg);
					state=ATTACK;
				}
			}
			return;
		}
		mwv=new m3().set_wv(new p3(),this.vhl.agl());
		if(state==ADJUSTING_HEIGHT){
			double a=turn_towards(waypoint.dst);
			boolean b=climb_towards(waypoint.dst);
			if(a<0.1&&b)
				state=MOVING;
		}
		if(state==MOVING){
			turn_towards(waypoint.dst);
			pitch_towards(waypoint.dst);
			climb_towards(waypoint.dst);
			move_towards(waypoint.dst);
		}
		if(state==ATTACK){
			if(!trg.alive()){
				vhl.btn_fire(false);
				state=STANDBY;
				return;
			}
			p3 tg_p=trg.pos();
			p3 v1=new p3(vhl.pos(),tg_p);
			p3 v2=mwv.axis_z();
			tg_dist=-v1.dotprod(v2);
			if(Math.abs(tg_dist)<atkrng){
				turn_towards(tg_p);
				pitch_towards(tg_p);
				if(tg_dist>0&&Math.abs(turn_dotprod)<attack_turn_err_margin&&Math.abs(pitch_dotprod)<attack_pitch_err_margin)
					vhl.btn_fire(true);
				else
					vhl.btn_fire(false);
			}else{
				trg=null;
				vhl.btn_turn(0);
				vhl.btn_pitch(0);
				vhl.btn_fire(false);
				state=STANDBY;
			}
		}
	}
}
