package d2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

final public class poly extends Polygon{
	static final long serialVersionUID=1;
	protected int[]indices;
	protected Color color;

	public poly(int[]ix,Color color){
		super(new int[ix.length],new int[ix.length],ix.length);
		indices=ix;
		this.color=color;
	}

	public void paint(Graphics graphics,int[]x,int[]y){
		for(int i=0;i<npoints;i++){
			xpoints[i]=x[indices[i]];
			ypoints[i]=y[indices[i]];
		}
		graphics.setColor(color);
		graphics.fillPolygon(this);
	}

	public int[]indices(){return indices;}
	public int nindices(){return npoints;}
	public Color color(){return color;}
	public void color(Color color){this.color=color;}
}
