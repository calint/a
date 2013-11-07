package d3;
import java.io.IOException;
import java.io.Serializable;
public final class pa3 implements Serializable,Cloneable{
	private static final long serialVersionUID=1L;
	int len;
	double[] x;
	double[] y;
	double[] z;
	public pa3(final xis xis) throws IOException{
		len=xis.int_read();
		x=new double[len];
		y=new double[len];
		z=new double[len];
		for(int n=0;n<len;n++){
			x[n]=xis.double_read();
			y[n]=xis.double_read();
			z[n]=xis.double_read();
		}
	}
	public pa3(final int n){
		len=n;
		x=new double[n];
		y=new double[n];
		z=new double[n];
	}
	public pa3(final int len0,final double[] x0,final double[] y0,final double[] z0){
		x=x0;
		y=y0;
		z=z0;
		len=len0;
	}
	public pa3 clone(){
		double[] xn=new double[len];
		double[] yn=new double[len];
		double[] zn=new double[len];
		System.arraycopy(x,0,xn,0,len);
		System.arraycopy(y,0,yn,0,len);
		System.arraycopy(z,0,zn,0,len);
		return new pa3(len,xn,yn,zn);
	}
	public String toString(){
		String str=new String();
		str=" "+len+"\n";
		for(int n=0;n<len;n++){
			str=str+x[n]+" "+y[n]+" "+z[n]+"\n";
		}
		return str;
	}
}
