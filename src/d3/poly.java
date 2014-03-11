package d3;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.IOException;
import java.io.Serializable;
final class poly implements Serializable,Cloneable{
	private static final int npi=16;
	private static final Polygon awtpoly=new Polygon(new int[npi],new int[npi],npi);
	private static final long serialVersionUID=1L;
	final static int awt_poly_detm(){
		int p1x=awtpoly.xpoints[1],p1y=awtpoly.ypoints[1];
		int v1x=awtpoly.xpoints[2]-p1x;
		int v1y=awtpoly.ypoints[2]-p1y;
		int v2x=awtpoly.xpoints[0]-p1x;
		int v2y=awtpoly.ypoints[0]-p1y;
		return v1x*v2y-v2x*v1y;
	}
	final static void awt_poly_fill(final poly p,final Graphics g){
		metrics.rend_npoly++;
		if(cfg.rend_pgon_cull)
			if(awt_poly_detm()>0)
				return;
		if(cfg.rend_wire){
			if(cfg.rend_wire_pen)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);
			g.drawPolygon(awtpoly);
		}
		if(!cfg.rend_solid)
			return;
		g.setColor(p.cl.awtcolor());
		g.fillPolygon(awtpoly);
	}
	private static final boolean clp_x_do(final double clpmgn,final int clpflg,final boolean in,final window c,final pa2z ps,final int i0,final int i1,final pa2z psc,final int k){
		double dx=ps.x[i1]-ps.x[i0];
		if(dx==0){
			if((ps.y[i1]-ps.y[i0])==0)
				return false;
		}
		double x=clpmgn;
		double t=(x-ps.x[i0])/dx;
		double y=ps.y[i0]+(ps.y[i1]-ps.y[i0])*t;
		psc.x[k]=(int)x;
		psc.y[k]=(int)y;
		if(in)
			psc.cf[k]=ps.cf[i1]-clpflg;
		else
			psc.cf[k]=ps.cf[i1];
		return true;
	}
	private static final boolean clp_y_do(final double clpmgn,final int clpflg,final boolean in,final window c,final pa2z ps,final int i0,final int i1,final pa2z psc,final int k){
		int dy=ps.y[i1]-ps.y[i0];
		int y=(int)clpmgn;
		if(dy==0){
			if((ps.x[i1]-ps.x[i0])==0)
				return false;
		}
		double t=(double)(y-ps.y[i0])/dy;
		int x=ps.x[i0]+(int)((double)(ps.x[i1]-ps.x[i0])*t);
		psc.x[k]=x;
		psc.y[k]=y;
		if(in)
			psc.cf[k]=ps.cf[i1]-clpflg;
		else
			psc.cf[k]=ps.cf[i1];
		return true;
	}
	color cl;
	final int[] clpf={window.CLP_Z,window.CLP_LFT,window.CLP_RHT,window.CLP_TOP,window.CLP_BTM};
	final byte[] clpt={'z','x','x','y','y'};
	p3 nm;
	int[] pi;
	public poly(final xis xis) throws IOException{
		int len=xis.int_read();
		pi=new int[len];
		for(int i=0;i<len;i++){
			pi[i]=xis.int_read();
		}
		cl=new color(xis);
	}
	public poly(final int[] pi0,final color cl0){
		pi=pi0;
		cl=cl0;
	}
	final p3 calc_nml(final pa3 a){
		int i0=pi[0];
		int i1=pi[1];
		int i2=pi[pi.length-1];
		p3 p0=new p3(a.x[i0],a.y[i0],a.z[i0]);
		p3 v1=new p3(p0,new p3(a.x[i1],a.y[i1],a.z[i1]));
		p3 v2=new p3(p0,new p3(a.x[i2],a.y[i2],a.z[i2]));
		p3 nml=new p3().vecprod(v1,v2).norm();
		return nml;
	}
	public poly clone(){
		int[] i=new int[pi.length];
		System.arraycopy(pi,0,i,0,pi.length);
		return new poly(i,cl.clone());
	}
	private boolean clp(final int side,final byte clptype,final double clpmgn,final int clpflg,final window wn,final pa2z ps,final pa2z psc,final pa3 pv){
		metrics.rend_poly_clp[side]++;
		psc.n=0;
		int i0=ps.n-1,i1=0;
		boolean in=(ps.cf[i0]&clpflg)==0;
		for(i1=0;i1<ps.n;i1++){
			boolean clp=(ps.cf[i1]&clpflg)!=0;
			if(clp){
				if(in){
					do_clptype(clptype,clpmgn,clpflg,wn,ps,psc,pv,i0,i1,in);
					in=false;
				}
			}else{
				if(!in){
					do_clptype(clptype,clpmgn,clpflg,wn,ps,psc,pv,i0,i1,in);
					in=true;
				}
				psc.x[psc.n]=ps.x[i1];
				psc.y[psc.n]=ps.y[i1];
				psc.cf[psc.n]=ps.cf[i1];
				psc.n++;
			}
			i0=i1;
		}
		return psc.n==0;
	}
	private final boolean clp_z_do(final double clpmgn,final int clpflg,final boolean in,final window c,final pa2z ps,final int i0,final int i1,final pa2z psc,final int k,final pa3 pv){
		int pt0=pi[i0];
		int pt1=pi[i1];
		double x0=pv.x[pt0];
		double y0=pv.y[pt0];
		double z0=pv.z[pt0];
		double x1=pv.x[pt1];
		double y1=pv.y[pt1];
		double z1=pv.z[pt1];
		double dz=z1-z0;
		if(dz==0)
			throw new IllegalStateException();
		double z=clpmgn;
		double t=(z-z0)/dz;
		double x=x0+(x1-x0)*t;
		double y=y0+(y1-y0)*t;
		psc.x[k]=(int)(c.scr_dst*x/z)+c.x0;
		psc.y[k]=(int)(c.scr_dst*y/z)+c.y0;
		psc.cf[k]=0;
		if(psc.x[k]>c.clp_rht){
			psc.cf[k]|=window.CLP_RHT;
		}else if(psc.x[k]<c.clp_lft){
			psc.cf[k]|=window.CLP_LFT;
		}else if(psc.y[k]>c.clp_btm){
			psc.cf[k]|=window.CLP_BTM;
		}else if(psc.y[k]<c.clp_top){
			psc.cf[k]|=window.CLP_TOP;
		}
		return true;
	}
	public final void color(final color c){
		this.cl=c;
	}
	private final void do_clptype(final byte clptype,final double clpmgn,final int clpflg,final window wn,final pa2z ps,final pa2z psc,final pa3 pv,int i0,int i1,boolean in){
		boolean adv=true;
		switch(clptype){
		case 'z':
			clp_z_do(clpmgn,clpflg,in,wn,ps,i0,i1,psc,psc.n,pv);
			break;
		case 'x':
			adv=clp_x_do(clpmgn,clpflg,in,wn,ps,i0,i1,psc,psc.n);
			break;
		case 'y':
			adv=clp_y_do(clpmgn,clpflg,in,wn,ps,i0,i1,psc,psc.n);
			break;
		}
		if(adv)
			psc.n++;
	}
	private void paint_noclp(final Graphics g,final pa2 ps){
		for(int n=0;n<pi.length;n++){
			int i=pi[n];
			awtpoly.xpoints[n]=ps.x[i];
			awtpoly.ypoints[n]=ps.y[i];
		}
		awtpoly.npoints=pi.length;
		awt_poly_fill(this,g);
	}
	public void render_clp(final Graphics g,final window wn,final pa3 pv,final pa2z ps,final double itn){
		cl.intensity(itn);
		if(ps.clp_allor==0){
			metrics.rend_poly_clpno++;
			paint_noclp(g,ps);
			return;
		}
		if(ps.clp_alland!=0){
			metrics.rend_poly_clp_cullcf++;
			return;
		}
		final double[] clpside={wn.clp_znear,wn.clp_lft,wn.clp_rht,wn.clp_top,wn.clp_btm};
		pa2z[] psc=new pa2z[]{new pa2z(pi.length*3),new pa2z(pi.length*3)};
		for(int k=0;k<pi.length;k++){
			psc[0].x[k]=ps.x[pi[k]];
			psc[0].y[k]=ps.y[pi[k]];
			psc[0].cf[k]=ps.cf[pi[k]];
		}
		psc[0].n=pi.length;
		int fr=0,to=0;
		for(int k=0;k<clpt.length;k++)
			if((ps.clp_allor&clpf[k])!=0){
				to=(fr+1)%2;
				psc[to].n=0;
				if(clp(k,clpt[k],clpside[k],clpf[k],wn,psc[fr],psc[to],pv)){
					metrics.rend_poly_clp_cull[k]++;
					return;
				}
				fr=to;
			}
		awtpoly.npoints=psc[to].n;
		System.arraycopy(psc[to].x,0,awtpoly.xpoints,0,awtpoly.npoints);
		System.arraycopy(psc[to].y,0,awtpoly.ypoints,0,awtpoly.npoints);
		awt_poly_fill(this,g);
	}
	public void render_noclp(final Graphics g,final pa2 ps,final double itn){
		cl.intensity(itn);
		paint_noclp(g,ps);
	}
	public String toString(){
		String str=Integer.toString(pi.length);
		for(int n=0;n<pi.length;n++){
			str=str+" "+pi[n];
		}
		return str;
	}
}
