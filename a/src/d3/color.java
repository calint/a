package d3;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
final class color implements Serializable,Cloneable{
	private static final long serialVersionUID=1L;
	private Color awtcolor;
	private int r,g,b;
	public color(xis xis) throws IOException{
		r=xis.int_read();
		g=xis.int_read();
		b=xis.int_read();
		awtcolor=new Color(r,g,b);
	}
	public color(int r0,int g0,int b0){
		r=r0;
		g=g0;
		b=b0;
		awtcolor=new Color(r,g,b);
	}
	public Color awtcolor(){
		return awtcolor;
	}
	public color clone(){
		return new color(r,g,b);
	}
	public void intensity(double i){
		int ii=(int)(i*64);
		int rr=r+ii,gg=g+ii,bb=b+ii;
		if(rr>255)
			rr=255;
		else if(rr<0)
			rr=0;
		if(gg>255)
			gg=255;
		else if(gg<0)
			gg=0;
		if(bb>255)
			bb=255;
		else if(bb<0)
			bb=0;
		awtcolor=new Color(rr,gg,bb);
	}
	public String toString(){
		return new String("color="+r+"  "+g+"  "+b);
	}
}
