package a.medusa.algebra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import a.medusa.medusa.inline;
import a.medusa.medusa.reads;
import a.medusa.medusa.self;
import a.medusa.medusa.takes;
import b.xwriter;

public class planes implements Serializable{
	public planes(final@takes plane...pn){
		for(final plane p:pn)
			list.add(p);
	}
	
	
	// 1: totally inside   2: totally outside   3: intersects
	public int check_collision_with_sphere(final@reads point sphere_position,final float sphere_radius){
		// if sphere totally outside planes in list => return 2 (cull)
		// if sphere totally inside planes in list => return 1 (render_noclip)
		// if sphere intersects => return 3 (render_clip(clipinfo))
		final AtomicInteger inside=new AtomicInteger();
		final AtomicInteger outside=new AtomicInteger();
		final AtomicInteger intersects=new AtomicInteger();
		list.parallelStream().forEach((pn)->{
			final int res=pn.check_collision_with_sphere(sphere_position,sphere_radius);
			switch(res){
			case 1:inside.incrementAndGet();break;
			case 2:outside.incrementAndGet();break;
			case 3:intersects.incrementAndGet();break;
			}
		});
		final int nplanes=list.size();
		if(inside.get()==nplanes)return 1;
		if(outside.get()==nplanes)return 2;
		return 3;
	}
	public boolean check_collision_with_planes(final@reads planes v){
		return false;
	}

	@inline final public @self planes add(final@takes plane p){list.add(p);return this;}
	final private ArrayList<plane>list=new ArrayList<>(6);
	
	
	///textilize
	public void to(final xwriter x){
		x.p("pns{[");
		list.forEach((p)->{p.to(x);});
		x.p("]}");
	}
	
	
	/// flyweight
	final static planes sqr=new planes(
			new plane(new point( 1, 0,0),new point( 1, 0,0),false),//? right plane
			new plane(new point(-1, 0,0),new point(-1, 0,0),false),//? left plane
			new plane(new point( 0, 1,0),new point( 0, 1,0),false),//? top plane
			new plane(new point( 0,-1,0),new point( 0,-1,0),false)//? bottom plane
		);
	
	public static void transform(final planes src,final planes dst,final matrix transform_matrix,final matrix normals_matrix){
		
	}
	
	
	private static final long serialVersionUID=1;
}
