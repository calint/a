package d3.game4;
import java.util.LinkedList;
import java.util.List;
import d3.f3;
import d3.obj;
import d3.objm;
import d3.p3;
import d3.world;
public final class expl extends objm{
	public static f3 polyh=new f3("/d3/game4/expl.f3d");
	public static final long serialVersionUID=1L;
	public double ds1;
	public double ds2;
	public double strength;
	public double time1;
	public double time2;
	public expl(world w,p3 p,double s0,double t0,double s1,double t1,double s2){
		super(w,new p3(p.x,s0,p.z),new p3(),new p3(),new p3(0,3,0),polyh,new p3(s0*2,s0*0.33,s0*2),type_scenery,1,t0+t1,true);
		strength=s1;
		time1=t0;
		ds1=(s1-s0)/t0;
		time2=t1;
		ds2=(s2-s1)/t1;
		new trace(this,wld.rand(0.5,1.0),4);
	}
	public double strength(){
		return strength;
	}
	public void upd_dt(double dt){
		super.upd_dt(dt);
		List<obj> ls=new LinkedList<obj>();
		p3 s=scale();
		double range=s.magnitude();
		wld.q_rng(obj.type_struc+obj.type_strucm+obj.type_vehicle,pos(),range,ls);
		for(obj o:ls)
			o.health(o.health()-strength*dt);
		double age=age();
		if(age>time1){
			s.add(ds2*dt);
		}else{
			s.add(ds1*dt);
		}
		s.abs();
		scale(s);
		p3 p=pos();
		p.y=s.y;
		pos(p);
	}
}
