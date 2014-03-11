package d3.game;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
import d3.world;
public class gate extends objm{
	final static long serialVersionUID=1L;
	public static f3 f3d=new f3(gate.class);
	public static p3 scl=new p3(8,5,2);
	public static double health=2000;
	public static double maxHeight=10;
	public static double speedDown0=10;
	public static double speedUp0=10;
	public static double waitTimeUp0=3;
	private double counter;
	private double height;
	private double originalPos;
	private double speedDown;
	private double speedUp;
	private int state;
	private double waitTimeUp;
	public gate(world w,p3 p,p3 a){
		super(w,new p3(p.x,p.y+scl.y,p.z),a,new p3(),new p3(),f3d,scl,type_strucm,health,Double.MAX_VALUE,true);
		state=0;
		originalPos=p.y;
		speedUp=speedUp0;
		speedDown=speedDown0;
		height=maxHeight;
		waitTimeUp=waitTimeUp0;
	}
	protected void on_hit(obj obj){
		if(state==0){
			state=1;
		}
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		if(state==1){
			p3 p=dpos();
			p.y=speedUp*dt;
			dpos(p);
			state=2;
		}else if(state==2){
			p3 p=pos();
			if(p.y-height>originalPos){
				p=dpos();
				p.y=0;
				dpos(p);
				state=3;
				counter=0;
			}
		}else if(state==3){
			counter=counter+dt;
			if(counter>waitTimeUp){
				p3 p=dpos();
				p.y=-speedDown*dt;
				dpos(p);
				state=4;
			}
		}else if(state==4){
			p3 p=pos();
			if(p.y<originalPos){
				p=dpos();
				p.y=0;
				dpos(p);
				state=0;
			}
		}
	}
}
