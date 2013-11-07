package d2;
import java.awt.Graphics;
final public class polyh{
	protected poly[]polys;
	protected int npolys;
	protected double[]x;
	protected double[]y;
	protected int npoints;

	public polyh(final double[]ptsx,final double[]ptsy,final poly[]pgs){
		if(pgs==null)
			return;
		x=ptsx;
		y=ptsy;
		npoints=ptsx.length;
		polys=pgs;
		npolys=pgs.length;
	}

	public polyh(polyh p){
		npoints=p.npoints();
		double[] xx=p.x();
		x=new double[npoints];
		System.arraycopy(xx,0,x,0,npoints);
		double[] yy=p.y();
		y=new double[npoints];
		System.arraycopy(yy,0,y,0,npoints);
		npolys=p.npolys();
		polys=new poly[npolys];
		poly[] po=p.polygons();
		for(int i=0;i<npolys;i++){
			int n=po[i].nindices();
			int[] is=new int[n];
			System.arraycopy(po[i].indices(),0,is,0,n);
			polys[i]=new poly(is,po[i].color());
		}
	}

	public double calculateBoundingRadius(){
		double d=0;
		for(int i=0;i<npoints;i++){
			double dd=x[i]*x[i]+2*x[i]*y[i]+y[i]*y[i];
			if(dd>d)d=dd;
		}
		return Math.sqrt(d);
	}

	public void paint(Graphics graphics,int[]xx,int[]yy){
		for(int i=0;i<npolys;i++)
			polys[i].paint(graphics,xx,yy);
	}

	public void scalePoints(double sx,double sy){
		for(int i=0;i<npoints;i++){
			x[i]*=sx;
			y[i]*=sy;
		}
	}

	public void rotate(double a){
		final double ca=Math.cos(a);
		final double sa=Math.sin(a);
		for(int i=0;i<npoints;i++){
			final double nx=x[i]*ca-y[i]*sa;
			final double ny=x[i]*sa+y[i]*ca;
			x[i]=nx;
			y[i]=ny;
		}
	}

	public int npolys(){return npolys;}
	public poly[] polygons(){return polys;}
	public double[] x(){return x;}
	public double[] y(){return y;}
	public int npoints(){return npoints;}
}
