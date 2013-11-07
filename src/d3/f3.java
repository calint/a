package d3;
import java.awt.Graphics;
import java.io.Serializable;
public final class f3 implements Serializable,Cloneable{
	private static final long serialVersionUID=1L;
	protected pa3 nmls;
	protected poly polys[];
	protected pa3 pts;
	public f3(xis xis){
		try{
			pts=new pa3(xis);
			polys=new poly[xis.int_read()];
			for(int n=0;n<polys.length;n++){
				polys[n]=new poly(xis);
			}
			calc_nmls();
		}catch(Throwable t){
			throw new Error(t);
		}
	}
	protected f3(pa3 pts0,poly[] polys0){
		pts=pts0;
		polys=polys0;
		calc_nmls();
	}
	protected f3(pa3 pts0,poly[] polys0,pa3 nmls0){
		pts=pts0;
		polys=polys0;
		nmls=nmls0;
	}
	public f3(String path){
		this(new xis(f3.class.getResourceAsStream(path)));
	}
	public f3(Class<? extends obj>cls){
		this("/"+cls.getName().replace('.','/')+".f3d");
	}
	public void calc_nmls(){
		nmls=new pa3(polys.length);
		for(int n=0;n<polys.length;n++){
			p3 norm=polys[n].calc_nml(pts);
			nmls.x[n]=norm.x;
			nmls.y[n]=norm.y;
			nmls.z[n]=norm.z;
		}
	}
	public void calc_shade(p3 lht,double[] itns){
		p3 p=new p3();
		for(int k=0;k<polys.length;k++){
			p.set(nmls.x[k],nmls.y[k],nmls.z[k]);
			itns[k]=p.dotprod(lht);
		}
	}
	public f3 clone(){
		poly[] p=new poly[polys.length];
		for(int n=0;n<p.length;n++)
			p[n]=polys[n].clone();
		return new f3(pts.clone(),p);
	}
	public pa3 pts(){
		return pts;
	}
	public void render_clp(Graphics g,window wn,pa3 pv,pa2z ps,double[] itns){
		for(int n=0;n<polys.length;n++)
			polys[n].render_clp(g,wn,pv,ps,itns[n]);
	}
	public void render_noclp(Graphics g,pa2 ps,double[] itns){
		for(int n=0;n<polys.length;n++)
			polys[n].render_noclp(g,ps,itns[n]);
	}
	public String toString(){
		String str=new String();
		str=pts.toString();
		str=str+polys.length+"\n";
		for(int n=0;n<polys.length;n++){
			str=str+polys[n].toString();
		}
		return str;
	}
}
