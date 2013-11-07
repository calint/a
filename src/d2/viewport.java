package d2;
final public class viewport{
	protected double X0;
	protected double Y0;
	protected double X1;
	protected double Y1;
	protected double Xorigo;
	protected double Yorigo;
	protected double theta;
	protected int W;
	protected int H;
	protected double scalex;
	protected double scaley;
	protected boolean topDown;

	public viewport(double x0,double y0,double x1,double y1,int wi,int hi,boolean bool){
		X0=x0;
		Y0=y0;
		X1=x1;
		Y1=y1;
		Xorigo=(X1-X0)/2.0;
		Yorigo=(Y1-Y0)/2.0;
		W=wi;
		H=hi;
		topDown=bool;
		theta=0.0;
		calcScale();
	}

	public void setTheta(double d){
		theta=d;
	}

	public int x(double d){
		return (int)((d-X0)*scalex);
	}

	public int y(double d){
		if(topDown) return (int)((d-Y0)*scaley);
		return (int)((double)H-(d-Y0)*scaley);
	}

	public double Xinv(int i){
		return X0+(double)i/scalex;
	}

	public double Yinv(int i){
		return Y0+(double)(H-i)/scaley;
	}

	public void center(double d,double d_4_){
		double d_5_=(X1-X0)/2.0;
		double d_6_=(Y1-Y0)/2.0;
		X0=d-d_5_;
		X1=d+d_5_;
		Y0=d_4_-d_6_;
		Y1=d_4_+d_6_;
	}

	public void setScreenSize(int i,int i_7_){
		if(W!=i){
			W=i;
			scalex=(double)W/(X1-X0);
		}
		if(H!=i_7_){
			H=i_7_;
			scaley=(double)H/(Y1-Y0);
		}
	}

	public void zoom(double s){
		X0=(X0-Xorigo)*s+Xorigo;
		X1=(X1-Xorigo)*s+Xorigo;
		Y0=(Y0-Yorigo)*s+Yorigo;
		Y1=(Y1-Yorigo)*s+Yorigo;
		calcScale();
	}

	public double scaleX(double d){
		return d*scalex;
	}

	public double scaleY(double d){
		return d*scaley;
	}

	public boolean circleInView(double x,double y,double r){
		if(x+r>X0&&x-r<X1&&y+r>Y0&&y-r<Y1)return true;
		return false;
	}

	protected void calcScale(){
		scalex=(double)W/(X1-X0);
		scaley=(double)H/(Y1-Y0);
	}
}
