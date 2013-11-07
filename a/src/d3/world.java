package d3;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
public final class world extends obj{
	private static final long serialVersionUID=1L;
	public double gravity;
	private grid[] grds;
	private double grdsz;public double grdsz(){return grdsz;}
	private int ngrds;
	private LinkedList<obj> objs;
	private LinkedList<obj> objsnew;
	private player plr;
	private Random rand=new Random(cfg.rnd_seed);
	private int rows;public int rows(){return rows;}
//	private double time;
	private double xmin;
	private double ymin;public double ymin(){return ymin;}
	private double width;public double width(){return width;}
	public world(double xmin0,double ymin0,double width0,int rows0,double gravity0){
		super(null,new p3(),new p3(),type_scenery,null,new p3(1.0),0);
		objsnew=new LinkedList<obj>();
		objs=new LinkedList<obj>();
		gravity=gravity0;
		rows=rows0;
		xmin=xmin0;
		ymin=ymin0;
		width=width0;
		ngrds=rows*rows;
		grds=new grid[ngrds];
		for(int n=0;n<ngrds;n++)
			grds[n]=new grid();
		grdsz=width0/rows;
		objsnew.add(this);
	}
	public final void add(obj obj){
		objsnew.add(obj);
//		obj.pt=this;
	}
	public double xmin(){
		return xmin;
	}
	private final int grdx(final double x){
		int xx=(int)((x-xmin)/grdsz);
		if(xx<0){
			return 0;
		}else if(xx>=rows){
			return rows-1;
		}else{
			return xx;
		}
	}
	private final int grdy(final double y){
		int yy=(int)((y-ymin)/grdsz);
		if(yy<0){
			return 0;
		}else if(yy>=rows){
			return rows-1;
		}else{
			return yy;
		}
	}
	public final Collection<obj> objects_all(){
		return objs;
	}
	//	public final int objects_size(){
	//		return objs.size();
	//	}
	public final p3 p3drnd(double s){
		return new p3(rand(-s,s),rand(-s,s),rand(-s,s));
	}
	public final p3 p3drnd(double b,double e){
		return new p3(rand(b,e),rand(b,e),rand(b,e));
	}
	public final p3 p3drnd(p3 p,double s){
		return new p3(p.x+rand(-s,+s),p.y+rand(-s,+s),p.z+rand(-s,+s));
	}
	public final player player(){
		return plr;
	}
	public final void player(player p){
		plr=p;
	}
	public final void q_rng(int flags,p3 p,double rng,List<obj> result){
		double x=p.x,y=p.z;
		int xstart=grdx(x-rng);
		int xend=grdx(x+rng);
		int ystart=grdy(y-rng);
		int yend=grdy(y+rng);
		for(int yy=ystart;yy<=yend;yy++)
			for(int xx=xstart;xx<=xend;xx++)
				grds[yy*rows+xx].q_rng(flags,p,rng,result);
	}
	public final double rand(double e){
		return e*rand.nextDouble();
	}
	public final double rand(double b,double e){
		return (e-b)*rand.nextDouble()+b;
	}
	//	public final double time(){
	//		return time;
	//	}
	public final void update(final double dt){
//		time+=dt;
		objs.addAll(objsnew);
		objsnew.clear();
		long t0=System.currentTimeMillis();
		update_grids();
		long t1=System.currentTimeMillis();
		metrics.wld_updgrds_ms=(int)(t1-t0);
		for(obj o:objs)
			o.upd_dt(dt);
		long t2=System.currentTimeMillis();
		metrics.wld_upd_ms=(int)(t2-t1);
		List<hit> hitls=new LinkedList<hit>();
		for(grid g:grds)
			g.coldet(hitls);
		long t3=System.currentTimeMillis();
		metrics.wld_coldet_ms=(int)(t3-t2);
		for(hit h:hitls){
			h.a.on_hit(h.b);
			h.b.on_hit(h.a);
		}
		long t4=System.currentTimeMillis();
		metrics.wld_colhdl_ms=(int)(t4-t3);
	}
	private final void update_grids(){
		for(Iterator<obj> i=objs.iterator();i.hasNext();){
			obj o=i.next();
			if(!o.upd_grds)
				continue;
			grid[] grdsnew=null;
			if(o.alive){
				double r=o.radius();
				int xb=grdx(o.pos.x-r);
				int xe=grdx(o.pos.x+r);
				int yb=grdy(o.pos.z-r);
				int ye=grdy(o.pos.z+r);
				int ngrds=(xe-xb+1)*(ye-yb+1);
				grdsnew=new grid[ngrds];
				int k=0;
				for(int yy=yb;yy<=ye;yy++)
					for(int xx=xb;xx<=xe;xx++){
						grid g=grds[yy*rows+xx];
						grdsnew[k++]=g;
						boolean found=false;
						for(int n=0;n<o.grds.length;n++){
							if(o.grds[n]==g){
								o.grds[n]=null;
								found=true;
								break;
							}
						}
						if(!found){
							g.put(o);
						}
					}
			}
			for(grid g:o.grds)
				if(g!=null)
					g.take(o);
			if(o.alive){
				o.grds=grdsnew;
				o.upd_grds=false;
			}else{
				i.remove();
			}
		}
	}
}
