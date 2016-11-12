
package d3;
import java.util.LinkedList;
import java.util.List;
final class grid{
	static final grid[]emptya=new grid[0];
	final private LinkedList<obj>bullets=new LinkedList<obj>();
	final private LinkedList<obj>scenery=new LinkedList<obj>();
	final private LinkedList<obj>strucms=new LinkedList<obj>();
	final private LinkedList<obj>strucs=new LinkedList<obj>();
	final private LinkedList<obj>vehicles=new LinkedList<obj>();
	final void clear(final int flags){
		if((flags&obj.type_struc)!=0)strucs.clear();
		if((flags&obj.type_strucm)!=0)strucms.clear();
		if((flags&obj.type_vehicle)!=0)vehicles.clear();
		if((flags&obj.type_scenery)!=0)scenery.clear();
		if((flags&obj.type_bullet)!=0)bullets.clear();
	}
	final void coldet(final List<hit>hitls){
		final int nvehicles=vehicles.size();
		obj a,b;
		if(nvehicles>1){
			for(int n=nvehicles-1;n>0;n--){
				a=vehicles.get(n);
				for(int m=n-1;m>=0;m--){
					b=vehicles.get(m);
					if(a==b)
						continue;
					final hit h=a.hitdet(b);
					if(h!=null)
						hitls.add(h);
				}
			}
		}
		for(int n=0;n<nvehicles;n++){
			a=vehicles.get(n);
			final int nstrucs=strucs.size();
			for(int m=0;m<nstrucs;m++){
				b=strucs.get(m);
				final hit h=b.hitdet(a);
				if(h!=null)
					hitls.add(h);
			}
			final int nstrucms=strucms.size();
			for(int m=0;m<nstrucms;m++){
				b=strucms.get(m);
				final hit h=b.hitdet(a);
				if(h!=null)
					hitls.add(h);
			}
		}
		final int nbullets=bullets.size();
		for(int n=0;n<nbullets;n++){
			a=bullets.get(n);
			final int nstrucs=strucs.size();
			for(int m=0;m<nstrucs;m++){
				b=strucs.get(m);
				final hit h=b.hitdet(a);
				if(h!=null)
					hitls.add(h);
			}
			final int nstrucms=strucms.size();
			for(int m=0;m<nstrucms;m++){
				b=strucms.get(m);
				final hit h=b.hitdet(a);
				if(h!=null)
					hitls.add(h);
			}
			for(int m=0;m<nvehicles;m++){
				b=vehicles.get(m);
				final hit h=b.hitdet(a);
				if(h!=null)
					hitls.add(h);
			}
		}
	}
	final void get_obj(final int flags,final List<obj>ls){
		if((flags&obj.type_struc)!=0)
			ls.addAll(strucs);
		if((flags&obj.type_strucm)!=0)
			ls.addAll(strucms);
		if((flags&obj.type_vehicle)!=0)
			ls.addAll(vehicles);
		if((flags&obj.type_scenery)!=0)
			ls.addAll(scenery);
		if((flags&obj.type_bullet)!=0)
			ls.addAll(bullets);
	}
	final void put(obj o){
		metrics.grd_put++;
		if((o.type&obj.type_struc)!=0)
			strucs.add(o);
		if((o.type&obj.type_strucm)!=0)
			strucms.add(o);
		if((o.type&obj.type_vehicle)!=0)
			vehicles.add(o);
		if((o.type&obj.type_scenery)!=0)
			scenery.add(o);
		if((o.type&obj.type_bullet)!=0)
			bullets.add(o);
	}
	public void q_rng(int flags,p3 p,double radius,List<obj> result){
		double radius2=radius*radius;
		if((flags&obj.type_struc)!=0)
			for(obj o:strucs)
				if(o.pos.distance2_to(p)<radius2)
					result.add(o);
		if((flags&obj.type_strucm)!=0)
			for(obj o:strucms)
				if(o.pos.distance2_to(p)<radius2)
					result.add(o);
		if((flags&obj.type_vehicle)!=0)
			for(obj o:vehicles)
				if(o.pos.distance2_to(p)<radius2)
					result.add(o);
		if((flags&obj.type_scenery)!=0)
			for(obj o:scenery)
				if(o.pos.distance2_to(p)<radius2)
					result.add(o);
		if((flags&obj.type_bullet)!=0)
			for(obj o:bullets)
				if(o.pos.distance2_to(p)<radius2)
					result.add(o);
	}
	final void take(obj o){
		metrics.grd_take++;
		if((o.type&obj.type_struc)!=0)
			strucs.remove(o);
		if((o.type&obj.type_strucm)!=0)
			strucms.remove(o);
		if((o.type&obj.type_vehicle)!=0)
			vehicles.remove(o);
		if((o.type&obj.type_scenery)!=0)
			scenery.remove(o);
		if((o.type&obj.type_bullet)!=0)
			bullets.remove(o);
	}
}
