package d3;
import java.io.Serializable;
public final class m3 implements Serializable,Cloneable{
	private static final long serialVersionUID=1L;
	private double xx=1,xy=0,xz=0,xo=0;
	private double yx=0,yy=1,yz=0,yo=0;
	private double zx=0,zy=0,zz=1,zo=0;
	final public p3 axis_x(){
		return new p3(xx,xy,xz);
	}
	final public p3 axis_y(){
		return new p3(yx,yy,yz);
	}
	final public p3 axis_z(){
		return new p3(zx,zy,zz);
	}
	private final m3 cat_rot_x(final double ax){
		double ct=Math.cos(ax),st=Math.sin(ax);
		double nyx=yx*ct+zx*st,nyy=yy*ct+zy*st,nyz=yz*ct+zz*st,nyo=yo*ct+zo*st;
		double nzx=zx*ct-yx*st,nzy=zy*ct-yy*st,nzz=zz*ct-yz*st,nzo=zo*ct-yo*st;
		yx=nyx;
		yy=nyy;
		yz=nyz;
		yo=nyo;
		zx=nzx;
		zy=nzy;
		zz=nzz;
		zo=nzo;
		return this;
	}
	private final m3 cat_rot_y(final double ay){
		double ct=Math.cos(ay),st=Math.sin(ay);
		double nxx=xx*ct+zx*st,nxy=xy*ct+zy*st,nxz=xz*ct+zz*st,nxo=xo*ct+zo*st;
		double nzx=zx*ct-xx*st,nzy=zy*ct-xy*st,nzz=zz*ct-xz*st,nzo=zo*ct-xo*st;
		xx=nxx;
		xy=nxy;
		xz=nxz;
		xo=nxo;
		zx=nzx;
		zy=nzy;
		zz=nzz;
		zo=nzo;
		return this;
	}
	//	public final m3 clone(){
	//		try{
	//			return (m3)super.clone();
	//		}catch(CloneNotSupportedException e){
	//			throw new IllegalStateException(e);
	//		}
	//	}
	//	public final void set_lookat(final p3 src,final p3 dst){
	//		p3 vz=new p3(src,dst).norm();
	//		p3 vx=new p3().vecprod(p3.axisy,vz);
	//		p3 vy=new p3().vecprod(vz,vx);
	//		xx=vx.x;
	//		xy=vx.y;
	//		xz=vx.z;
	//		xo=-src.x;
	//		yx=vy.x;
	//		yy=vy.y;
	//		yz=vy.z;
	//		yo=-src.y;
	//		zx=vz.x;
	//		zy=vz.y;
	//		zz=vz.z;
	//		zo=-src.z;
	//	}
	public final m3 concat_mw(final p3 p,final p3 a,final p3 s){
		return concat_scale(s).concat_rot_z(a.z).cat_rot_x(a.x).cat_rot_y(a.y).concat_translation(p);
	}
	private final m3 concat_rot_z(final double az){
		double ct=Math.cos(az),st=Math.sin(az);
		double nyx=yx*ct+xx*st,nyy=yy*ct+xy*st,nyz=yz*ct+xz*st,nyo=yo*ct+xo*st;
		double nxx=xx*ct-yx*st,nxy=xy*ct-yy*st,nxz=xz*ct-yz*st,nxo=xo*ct-yo*st;
		xx=nxx;
		xy=nxy;
		xz=nxz;
		xo=nxo;
		yx=nyx;
		yy=nyy;
		yz=nyz;
		yo=nyo;
		return this;
	}
	private final m3 concat_scale(final p3 s){
		xx*=s.x;
		xy*=s.x;
		xz*=s.x;
		xo*=s.x;
		yx*=s.y;
		yy*=s.y;
		yz*=s.y;
		yo*=s.y;
		zx*=s.z;
		zy*=s.z;
		zz*=s.z;
		zo*=s.z;
		return this;
	}
	private final m3 concat_translation(final p3 p){
		xo+=p.x;
		yo+=p.y;
		zo+=p.z;
		return this;
	}
	private final m3 concat_translation_neg(final p3 p){
		xo-=p.x;
		yo-=p.y;
		zo-=p.z;
		return this;
	}
	public final void mult(final p3 src,final p3 dst){
		double x=src.x*xx+src.y*xy+src.z*xz+xo;
		double y=src.x*yx+src.y*yy+src.z*yz+yo;
		double z=src.x*zx+src.y*zy+src.z*zz+zo;
		dst.x=x;
		dst.y=y;
		dst.z=z;
	}
	public final void mult(final pa3 src,final pa3 dst){
		for(int i=0;i<src.len;i++){
			double x=src.x[i],y=src.y[i],z=src.z[i];
			dst.x[i]=x*xx+y*xy+z*xz+xo;
			dst.y[i]=x*yx+y*yy+z*yz+yo;
			dst.z[i]=x*zx+y*zy+z*zz+zo;
		}
	}
	public final void mult(final pa3 src,final pa3 dst,final p3 offset){
		for(int i=0;i<src.len;i++){
			double x=src.x[i]+offset.x,y=src.y[i]+offset.y,z=src.z[i]+offset.z;
			dst.x[i]=x*xx+y*xy+z*xz+xo;
			dst.y[i]=x*yx+y*yy+z*yz+yo;
			dst.z[i]=x*zx+y*zy+z*zz+zo;
		}
	}
	public final m3 reset(){
		xx=1;
		xy=0;
		xz=0;
		xo=0;
		yx=0;
		yy=1;
		yz=0;
		yo=0;
		zx=0;
		zy=0;
		zz=1;
		zo=0;
		return this;
	}
	public final void rotate(final pa3 src,final pa3 dst){
		for(int i=0;i<src.len;i++){
			double x=src.x[i],y=src.y[i],z=src.z[i];
			dst.x[i]=x*xx+y*xy+z*xz;
			dst.y[i]=x*yx+y*yy+z*yz;
			dst.z[i]=x*zx+y*zy+z*zz;
		}
	}
	public final m3 set_mw(final p3 p,final p3 a,final p3 s){
		return reset().concat_mw(p,a,s);
	}
	public final m3 set_wv(final p3 p,final p3 a){
		return reset().concat_translation_neg(p).cat_rot_y(-a.y).cat_rot_x(-a.x).concat_rot_z(-a.z);
	}
}
