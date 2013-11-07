package d3.game4;
import d3.f3;
import d3.objm;
import d3.p3;
import d3.world;
public class bulletshell extends objm{
	private static final long serialVersionUID=1L;
	protected bulletshell(world w,p3 origin,p3 agl,p3 dpos,p3 dagl,double spread,double rot,double lifeTime0,f3 ph,p3 s){
		super(w,origin,agl,new p3(dpos.x+w.rand(-spread,spread),dpos.y+w.rand(-spread,spread),dpos.z+w.rand(-spread,spread)),new p3(dagl.x+w.rand(-rot,rot),dagl.y+w.rand(-rot,rot),dagl.z+w.rand(-rot,rot)),ph,s,type_scenery,1,lifeTime0,true);
	}
}
