package d3;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
public abstract class obj implements Serializable{
	public static boolean enable_forcess=true;
	
	public final static pa3 abox=new pa3(8,new double[]{1,-1,-1,1,-1,1,1,-1},new double[]{1,-1,1,-1,1,-1,1,-1},new double[]{1,-1,1,-1,-1,1,-1,1});
	public final static pa3 abox_nmls=new pa3(6,new double[]{0,0,-1,1,0,0},new double[]{1,-1,0,0,0,0},new double[]{0,0,0,0,-1,1});
	public static final long serialVersionUID=1L;
	public final static int type_bullet=4;
	public final static int type_scenery=8;
	public final static int type_struc=1;
	public final static int type_strucm=16;
	public final static int type_vehicle=2;
	public static double oclock(int h){
		return Math.PI/6*(h%12);
	}
	protected p3 agl;
	protected boolean alive;
	private pa3 box;
	private pa3 box_nmls;
	private double bradius;
	protected grid[] grds;
	protected List<hit> hits;
	private double hlth;
	private double itns[];
	private p3 lght;
	private p3 lookv;
	private m3 mw;
//	private long mw_ts;
//	private obj pt;
//	private m3 mwv(){upd_mw();return mw;}

	private f3 f3;
	protected p3 pos;
	protected obj pttype;
	private pa3 pw;
	protected p3 scl;
	public int type;
	protected boolean upd_pw,upd_mw,upd_lookvec,upd_itn,upd_nmls,upd_box,upd_grds;
	protected world wld;
	public obj(final world w,final p3 p,final p3 a,final int type0,final f3 f3,final p3 scl0,final double health0){
		wld=w;
		pos=p;
		agl=a;
		alive=true;
		hits=new LinkedList<hit>();
		type=type0;
		this.f3=f3;
		scl=scl0;
		bradius=scl.magnitude();
		hlth=health0;
		mw=new m3();
		lookv=new p3();
		lght=new p3();
		if(f3!=null){
			pw=new pa3(f3.pts.len);
			itns=new double[f3.polys.length];
		}
		box=new pa3(8);
		box_nmls=new pa3(6);
		grds=grid.emptya;
		upd_grds=upd_mw=upd_lookvec=upd_box=upd_pw=upd_nmls=upd_itn=true;
		if(w!=null)
			w.add(this);
	}
	public final p3 agl()/* const */{
		return agl.clone();
	}
	public final void agl(final p3 a){
		if(agl.equals(a))
			return;
		agl.set(a);
		upd_mw=upd_lookvec=upd_box=upd_pw=upd_nmls=upd_itn=true;
	}
	public final boolean alive(){
		return alive;
	}
	public final p3 axis_z()/* const */{
		upd_mw();
		return mw.axis_z();
	}
	public final double bradius()/* const */{
		return bradius;
	}
	public final double bradius2()/* const */{
		return bradius*bradius;
	}
	public void death(){
		if(!alive)
			throw new IllegalStateException();
		alive=false;
		upd_grds=true;
	}
	private final int dot_in_box(final pa3 pa){
		p3 nml=new p3();
		p3 pt=new p3();
		p3 v=new p3();
		for(int k=0;k<8;k++){
			pt.set(pa.x[k],pa.y[k],pa.z[k]);
			boolean outside=false;
			for(int i=0;i<6;i++){
				nml.set(box_nmls.x[i],box_nmls.y[i],box_nmls.z[i]);
				v.set(box.x[i],box.y[i],box.z[i]);
				v.vecto(pt);
				if(nml.dotprod(v)>0){
					outside=true;
					break;
				}
			}
			if(outside==false)
				return k;
		}
		return -1;
	}
//	private final int dot_in_f3(final pa3 pa){
//		upd_pw();
//		upd_nw();
//		return -1;
//	}
//	private final void upd_nw(){
//	}
	public final double health(){
		return hlth;
	}
	public final void health(final double h){
		hlth=h;
		if(hlth<0&&alive)
			death();
	}
	final hit hitdet(final obj o){
		metrics.coldet[0]++;
		final p3 p=pos();
		p.negate();
		p.add(o.pos());
		final double dist=p.magnitude();
		if(dist>(bradius+o.bradius)){
			return null;
		}
		upd_box();
		o.upd_box();
		int a=dot_in_box(o.box);
		if(a!=-1){
			metrics.coldethit[0]++;
			return new hit(this,o,a);
		}
		int b=o.dot_in_box(box);
		if(b!=-1){
			metrics.coldethit[0]++;
			return new hit(o,this,b);
		}
		return null;
	}
	public final p3 lookvec(){
		if(!upd_lookvec)
			return lookv.clone();
		lookv.set(0,0,1);
		lookv.rot_axis_x(-agl.x).rot_axis_y(agl.y);
		upd_lookvec=false;
		return lookv.clone();
	}
	protected void on_hit(final obj o){}
	public final f3 f3(){
		return f3;
	}
	public final p3 pos(){
		return pos.clone();
	}
	public final void pos(final p3 p){
		if(pos.equals(p))
			return;
		pos.set(p);
		upd_grds=upd_mw=upd_box=upd_pw=true;
	}
//	private boolean pt_upd(){
//		for(obj o=this;o.pt!=null;o=o.pt)
//			if(o.pt==null)
//				return false;
//			else if(o.upd_pw)
//				return true;
//		return false;
//	}
	public final double radius(){
		return bradius;
	}
	public final double radius2(){
		return bradius*bradius;
	}
	private void rend_bsph(Graphics g,window c){//?
	//		p3 p=pos();
	//		p3 ps1=c.wcs_to_scs(p);
	//		g.setColor(Color.gray);
	//		p3 radius=new p3(p.x+bradius,);
	//		p3 ps2=c.wcs_to_scs(radius);
	//		g.drawOval((int)(ps1.x-ps2.x),(int)(ps1.y-ps2.y),(int)(ps2.x*2),(int)(ps2.y*2));
	}
	public void rend_clp(final Graphics g,final window w,final p3 lht,final int clp){
		if(f3==null)
			return;
		upd_pw();
		upd_itn(lht);
		w.wcs_to_vcs(pw,w.pvbuf);
		w.vcs_proj(w.pvbuf,w.psbuf);
		w.clpflg(w.psbuf,clp);
		if(w.psbuf.clp_alland!=0){
			metrics.obj_rend_cull_proj++;
			return;
		}
		if(w.psbuf.clp_allor!=0){
			f3.render_clp(g,w,w.pvbuf,w.psbuf,itns);
			metrics.obj_rend_clp++;
			return;
		}
		f3.render_noclp(g,w.psbuf,itns);
		metrics.obj_rend_clpno++;
		// if(fx.obj_rend_bsphere)
		// render_bsphere(g,c);
	}
	public void rend_noclp(final Graphics g,final window w,final p3 lht){
		if(f3==null)
			return;
		upd_pw();
		upd_itn(lht);
		w.wcs_to_vcs(pw,w.pvbuf);
		w.vcs_proj(w.pvbuf,w.psbuf);
		f3.render_noclp(g,w.psbuf,itns);
		metrics.rend_noclp++;
		if(cfg.obj_rend_bsphere)
			rend_bsph(g,w);
	}
	public final void rotate(final pa3 src,final pa3 dst){
		upd_mw();
		mw.rotate(src,dst);
	}
	public final p3 scale()/* const */{
		return scl.clone();
	}
	public final void scale(final p3 s0){
		scl.set(s0);
		upd_grds=upd_mw=upd_box=upd_pw=upd_nmls=upd_itn=true;
	}
	public final p3 to_wcs(final p3 p){
		p3 pp=new p3();
		upd_mw();
		mw.mult(p,pp);
		return pp;
	}
	public void to_wcs(final p3 src,final p3 dst){
		upd_mw();
		mw.mult(src,dst);
	}
	public void to_wcs(pa3 src,pa3 dst){
		upd_mw();
		mw.mult(src,dst);
	}
	private final void upd_box(){
		if(!upd_box)
			return;
		to_wcs(abox,box);
		rotate(abox_nmls,box_nmls);
		upd_box=false;
	}
	public void upd_dt(final double dt){}
	private final void upd_itn(final p3 light){
		if(!lght.equals(light)){
			lght.set(light);
			upd_itn=true;
		}
		if(!upd_itn)
			return;
		f3.calc_shade(light,itns);
		upd_itn=false;
	}
	private final boolean upd_mw(){
		if(!upd_mw)
			return false;
		mw.set_mw(pos,agl,scl);
		upd_mw=false;
		return true;
	}
	private final void upd_pw(){
//		if(pt_upd())
//			upd_pw=true;
		if(!upd_pw)
			return;
		upd_mw();
		mw.mult(f3.pts(),pw);
		upd_pw=false;
	}
	public final world world(){
		return wld;
	}
}
