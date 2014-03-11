package d2;
import java.awt.Color;
import java.awt.Graphics;
final public class polyhi{
	protected polyh polyh;
	protected double[]xw;
	protected double[]yw;
	protected int[]xv;
	protected int[]yv;
	protected int npoints;
	double x;
	double y;
	double angle;

	public polyhi(polyh ph){
		polyh=ph;
		npoints=polyh.npoints();
		xw=new double[npoints];
		yw=new double[npoints];
		xv=new int[npoints];
		yv=new int[npoints];
	}

	public void transformWorldToScreen(viewport vp){
		for(int i=0;i<npoints;i++){
			xv[i]=vp.x(xw[i]);
			yv[i]=vp.y(yw[i]);
		}
	}

	public void transformLocalToWorld(){
		final double[]phx=polyh.x();
		final double[]phy=polyh.y();
		final double cos=Math.cos(angle);
		final double sin=Math.sin(angle);
		for(int i=0;i<npoints;i++){
			xw[i]=phx[i]*cos-phy[i]*sin+x;
			yw[i]=phx[i]*sin+phy[i]*cos+y;
		}
	}

	public void setStates(final double x,final double y,final double a){this.x=x;this.y=y;this.angle=a;}
	public void paint(final Graphics g,final viewport vp){transformWorldToScreen(vp);polyh.paint(g,xv,yv);}
	public void setPolygonColor(final int i,final Color color){polyh.polygons()[i].color(color);}
	public void scalePoints(final double sx,final double sy){polyh.scalePoints(sx,sy);}

	public void distortPoints(final double dx,final double dy){
		double[]x=polyh.x();
		double[]y=polyh.y();
		for(int i=0;i<npoints;i++){
			x[i]+=Math.random()*dx;
			y[i]+=Math.random()*dy;
		}
	}

	public double[]getXworld(){return xw;}
	public double[]getYworld(){return yw;}
	public int npoints(){return npoints;}
	public polyh polyh(){return polyh;}
	public double getXforPoint(final int i){return xw[i];}
	public double getYforPoint(final int i){return yw[i];}
}
