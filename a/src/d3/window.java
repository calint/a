package d3;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
final public class window extends objm{
	final static int CLP_TOP=1,CLP_BTM=2,CLP_RHT=4,CLP_LFT=8,CLP_Z=16;
	final static int CULLED=-1,NOCLP=0,CLP=1;
	static final long serialVersionUID=1;
	private Color cl_spc;
	int clp_btm;
	int clp_lft;
	private int clp_mgn=32;
	int clp_rht;
	int clp_top;
	private double clp_zfar;
	double clp_znear;
	private int gnd_npts;
	private int hght;
	private p3 lht;
	private m3 mwv;
	private pa3 paw;
	pa2z psbuf=new pa2z(1024);
	pa3 pvbuf=new pa3(1024);
	private p3 pyr_nml_bhn;
	private p3 pyr_nml_btm;
	private p3 pyr_nml_lft;
	private p3 pyr_nml_rht;
	private p3 pyr_nml_top;
	private p3 ra;
	private p3 rp;
	double scr_dst;
	private double speedp;
	private double sqsize;
	private obj trkobj;
	private boolean upd_mx;
	private boolean upd_pyr;
	private int wdth;
	int x0;
	int y0;
	//	private double zfade;
	public window(world wld,Dimension dim0,double scrdst0,obj trkobj0,p3 rp0,p3 ra0,double sp0,double znear0,double zfar0,double zfade0,double sqsize0){
		super(wld,new p3(),new p3(),new p3(),new p3(),null,new p3(),obj.type_scenery,0,Double.MAX_VALUE,true);
		scr_dst=scrdst0;
		clp_zfar=zfar0;
		clp_znear=znear0;
		set_dimension(dim0);
		mwv=new m3();
		upd_mx=true;
		speedp=sp0;
		trkobj=trkobj0;
		rp=rp0;
		lht=new p3(0,-1,0);
//		lht.rot_axis_x(Math.PI/5);
//		lht.rot_axis_y(Math.PI/3);
		sqsize=sqsize0;
		//		zfade = zfade0;
		int pts=(int)(2*zfar0/sqsize)+1;
		if(sqsize!=0){
			gnd_npts=pts*pts;
			paw=new pa3(gnd_npts);
			int n=0;
			for(double x=-clp_zfar;x<=clp_zfar;x+=sqsize){
				for(double z=-clp_zfar;z<=clp_zfar;z+=sqsize){
					paw.x[n]=x;
					paw.y[n]=0;
					paw.z[n]=z;
					n++;
				}
			}
			if(pvbuf.len<gnd_npts){
				pvbuf=new pa3(gnd_npts);
				psbuf=new pa2z(gnd_npts);
			}
		}
//		cl_sky=new Color(50,50,200);
//		cl_gnd=new Color(50,150,50);
		cl_spc=new Color(0x020408);
//		cl_dts=new Color(0x80,0xb0,0xb0);
		ra=ra0.negate();
	}
	public final Color cl_spc(){return cl_spc;}
	public final window cl_spc(int r,int g,int b){cl_spc=new Color(r,g,b);return this;}
	private final int clip_check(final obj o,final p3 v,final p3 nml,final double r,final double clpmgn){
		final double s=nml.dotprod(v);
		if(s<0){
			if(s<-r){return NOCLP;}else{return CLP;}
		}else{
			if(s<r){return CLP;}else{return CULLED;}
		}
	}
	final void clpflg(final pa2z ps,final int scf){
		ps.clp_alland=0xffffffff;
		ps.clp_allor=0;
		for(int n=0;n<ps.n;n++){
			int cf=0;
			if(ps.x[n]>clp_rht)
				cf|=CLP_RHT;
			else if(ps.x[n]<clp_lft)
				cf|=CLP_LFT;
			if(ps.y[n]>clp_btm)
				cf|=CLP_BTM;
			else if(ps.y[n]<clp_top)
				cf|=CLP_TOP;
			if(pvbuf.z[n]>clp_znear)
				cf|=CLP_Z;
			ps.clp_allor|=cf;
			ps.clp_alland&=cf;
			ps.cf[n]=cf;
		}
	}
	private final int is_in_viewfurst(final obj o){
		int clp;
		final p3 p=o.pos();
		final double r=o.bradius();
		final p3 v=new p3(pos,p);// vector to object position
		int allclp=0;
		clp=clip_check(o,v,pyr_nml_bhn,r,clp_znear);
		if(clp==CULLED){
			metrics.cam_cull_front++;
			return CULLED;
		}else if(clp==NOCLP)
			metrics.cam_front++;
		else{
			allclp|=1;
			metrics.cam_clip_front++;
		}
		clp=clip_check(o,v,pyr_nml_lft,r,0);
		if(clp==CULLED){
			metrics.cam_cull_left++;
			return CULLED;
		}else if(clp==NOCLP)
			metrics.cam_left++;
		else{
			allclp|=2;
			metrics.cam_clip_left++;
		}
		clp=clip_check(o,v,pyr_nml_rht,r,0);
		if(clp==CULLED){
			metrics.cam_cull_right++;
			return CULLED;
		}else if(clp==NOCLP)
			metrics.cam_right++;
		else{
			allclp|=4;
			metrics.cam_clip_right++;
		}
		clp=clip_check(o,v,pyr_nml_top,r,0);
		if(clp==CULLED){
			metrics.cam_cull_top++;
			return CULLED;
		}else if(clp==NOCLP)
			metrics.cam_top++;
		else{
			allclp|=8;
			metrics.cam_clip_top++;
		}
		clp=clip_check(o,v,pyr_nml_btm,r,0);
		if(clp==CULLED){
			metrics.cam_cull_btm++;
			return CULLED;
		}else if(clp==NOCLP)
			metrics.cam_btm++;
		else{
			allclp|=16;
			metrics.cam_clip_btm++;
		}
		return allclp;
	}
	public final void light(final p3 l){lht=l;}
	final void paint(final Graphics g){
		long t1=System.currentTimeMillis();
		if(cfg.rend_spc){
			g.setColor(cl_spc);
			g.fillRect(clp_lft,clp_top,clp_rht,clp_btm);
		}
		long t2=System.currentTimeMillis();
		long t3=System.currentTimeMillis();
		paint_obj(g);
		long t4=System.currentTimeMillis();
		metrics.cam_sky_ms=(int)(t2-t1);
		metrics.cam_gnd_ms=(int)(t3-t2);
		metrics.cam_obj_ms=(int)(t4-t3);
	}
	private final void paint_obj(final Graphics g){
		final long t1=System.currentTimeMillis();
		final List<obj>ols=new LinkedList<obj>();
		ols.addAll(wld.objects_all());
		metrics.cam_objq=ols.size();
		final List<pntle>pls=q(ols.iterator());
		final long t2=System.currentTimeMillis();
		metrics.cam_cull_ms=(int)(t2-t1);
		
		Collections.sort(pls);
		final long t3=System.currentTimeMillis();
		metrics.cam_sort_ms=(int)(t3-t2);

		for(final pntle e:pls)
			if(e.clp!=0)
				e.o.rend_clp(g,this,lht,e.clp);
			else
				e.o.rend_noclp(g,this,lht);
		final long t4=System.currentTimeMillis();
		metrics.cam_rend_ms=(int)(t4-t3);
		metrics.cam_rendtot=pls.size();
	}
	final public ArrayList<pntle>q(final Iterator<obj>i){
		upd_pyr();
		final ArrayList<pntle>pntls=new ArrayList<pntle>(4096);
		for(;i.hasNext();){
			final obj o=i.next();
			int clp=is_in_viewfurst(o);
			if(clp==CULLED){
				i.remove();
				continue;
			}
			final pntle e=new pntle(o,pos.distance2_to(o.pos)+o.bradius(),clp);
			pntls.add(e);
		}
		return pntls;
	}
	final p3 relagl(){return ra.clone();}
	final void relagl(final p3 a){ra.set(a);}
	final p3 relpos(){return rp.clone();}
	final void relpos(final p3 p){rp.set(p);}
	final void set_dimension(final Dimension dim){
		wdth=dim.width;
		hght=dim.height;
		clp_lft=clp_mgn;
		clp_rht=wdth-clp_mgn;
		clp_top=clp_mgn;
		clp_btm=hght-clp_mgn;
		x0=wdth>>1;
		y0=hght>>1;
		upd_pyr=true;
//		System.out.println(wdth+"x"+hght);
	}
	final window track_object(final obj o){trkobj=o;return this;}
	final public void upd_dt(final double dt){
		super.upd_dt(dt);
		if(upd_nmls)
			upd_mx=upd_pyr=true;
		p3 pt=new p3();
		trkobj.to_wcs(rp,pt);
		p3 p=pos().vecto(pt).scale(speedp*dt).add(pos());
		p3 aa=trkobj.agl().add(ra);
		p3 a=agl().vecto(aa).scale(speedp*dt).add(agl());
		pos(p);
		agl(a);
	}
	private final void upd_mx(){
		if(!upd_mx)
			return;
		mwv.set_wv(pos,agl);
		upd_mx=false;
	}
	private final void upd_pyr(){
		if(!upd_pyr)
			return;
//		System.out.println("updatepyr "+world().time());
		upd_mx();
		final p3[]scr=new p3[]{
				new p3(-x0,y0,scr_dst).norm(),// topleft
				new p3(-x0,-y0,scr_dst).norm(),// btmleft
				new p3(x0,y0,scr_dst).norm(),// toprght
				new p3(x0,-y0,scr_dst).norm(),// btmrght
		};
		final m3 m=new m3();
		m.set_mw(new p3(),agl,new p3(1));
		upd_mw=false;
		for(final p3 p:scr)
			m.mult(p,p);
		pyr_nml_bhn=mwv.axis_z();
		pyr_nml_lft=new p3().vecprod(scr[0],scr[1]);
		pyr_nml_rht=new p3().vecprod(scr[3],scr[2]);
		pyr_nml_top=new p3().vecprod(scr[1],scr[3]);
		pyr_nml_btm=new p3().vecprod(scr[2],scr[0]);
		upd_pyr=false;
	}
	public final void wcs_to_vcs(final pa3 pw,final pa3 pv){
		upd_mx();
		mwv.mult(pw,pv);
		pv.len=pw.len;
	}
	public final void vcs_proj(final pa3 pv,final pa2 ps){
		for(int n=0;n<pv.len;n++){
			final double z=pv.z[n];
			ps.x[n]=(int)(scr_dst*pv.x[n]/z)+x0;
			ps.y[n]=(int)(scr_dst*pv.y[n]/z)+y0;
		}
		ps.n=pv.len;
	}
}
