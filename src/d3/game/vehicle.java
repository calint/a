package d3.game;
import java.util.*;
import d3.*;
public class vehicle extends objm implements player{
	final static long serialVersionUID=1;
	public String toString(){return getClass().getName()+"@"+Integer.toHexString(hashCode())+"("+ai_off+","+ai+")";}
	
	private ai ai;
	private boolean ai_off;
	private double facc;
	private double fbrake;
	private double fdecent;
	double flev;
	private double fpitch;
	private double fturn;
	private byte[] keys=new byte[32];
	private double topspeed;
	private double trace_sec=0.4;
	private double trace_t;
	public boolean tracer=true;
	protected List<weapon> wpns;
	private int wpnsel;
	protected vehicle(world w,p3 p,p3 a,double fturn0,double fpitch0,double facc0,double fbrake0,double topspeed0,double flev0,double fdecent0,double health0,f3 f3,p3 s,boolean hoverer,ai ai){
		super(w,p,a,new p3(),new p3(),f3,s,type_vehicle,health0,Double.MAX_VALUE,hoverer);
		fturn=fturn0;
		fpitch=fpitch0;
		facc=facc0;
		fbrake=fbrake0;
		topspeed=topspeed0;
		flev=flev0;
		fdecent=fdecent0;
		wpnsel=1;
		wpns=new LinkedList<weapon>();
		this.ai=ai;
		if(this.ai!=null)this.ai.vhl(this);
	}
	public vehicle ai(final ai ai){this.ai=ai;return this;}
	public ai ai(){return ai;}
	public void btn_brake(final double p){keys[3]=(byte)(p*127);}
	public void btn_climb(final double p){keys[4]=(byte)(p*127);}
	public void btn_decent(final double p){keys[5]=(byte)(p*127);}
	public void btn_fire(final boolean on){keys[0]=(byte)(on?(wpnsel+1):0);}
	public void btn_gas(final double p){keys[2]=(byte)(p*127);}
	public void btn_pitch(final double p){keys[6]=(byte)(p*127);}
	public void btn_turn(final double p){keys[1]=(byte)(p*127);}
	public void do_wpnsel(final int i){if(i>-1&&i<wpns.size()){wpnsel=i;}}
	final public byte[]keys(){return keys;}
	
	protected void do_turn(final double p,final double dt){p3 a=agl();a.y+=p*dt*fturn;agl(a);}
	protected void do_pitch(final double p,final double dt){p3 a=agl();a.x+=p*dt*fpitch;agl(a);}
	protected void do_accel(final double p,final double dt){
		final p3 v=lookvec().negate();
		final p3 dv=dpos();
		if(dv.magnitude2()>topspeed*topspeed)return;
		dv.add(v.scale(p*dt*facc));
		dpos(dv);
	}
	protected void do_brake(final double p,final double dt){dpos(dpos().scale(1.0-p*dt*fbrake));}
	protected void do_climb(final double p,final double dt){p3 pt=pos();pt.y+=p==0?0:flev*p*dt;pos(pt);}
	protected void do_decent(final double p,final double dt){p3 pt=pos();pt.y-=fdecent*p*dt;pos(pt);}

	protected void on_hit(final obj o){
		final int tp=type_struc+type_strucm+type_vehicle;
		if((o.type&tp)!=0){
			restore_prev_state();
			dpos(dpos().zero());
			return;
		}
		super.on_hit(o);
	}
	public void upd_dt(final double dt){
		super.upd_dt(dt);
		if(ai!=null&&!ai_off)ai.update(dt);
		for(final weapon w:wpns)w.update(dt);
		byte b;
		b=keys[0];if(b!=0)wpns.get(wpnsel-1).fire();
		b=keys[1];if(b!=0)do_turn(1.0/127*b,dt);
		b=keys[2];if(b!=0)do_accel(1.0/127*b,dt);
		b=keys[3];if(b!=0)do_brake(1.0/127*b,dt);
		b=keys[4];if(b!=0)do_climb(1.0/127*b,dt);
		b=keys[5];if(b!=0)do_decent(1.0/127*b,dt);
		b=keys[6];if(b!=0)do_pitch(1.0/127*b,dt);
		b=keys[8];if(b!=0){ai_off=false;System.out.println("ai: off");}
		b=keys[9];if(b!=0){ai_off=true;System.out.println("ai: on");}
		if(!cfg.vhcl_trace)	return;
		if(!tracer)return;
		trace_t+=dt;
		if(trace_t>trace_sec){
			new trace(this,1.2,3);
			trace_t-=trace_sec;
		}
	}
}
