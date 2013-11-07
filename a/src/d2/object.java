package d2;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
public abstract class object{
	private boolean isalive;
	protected world wld;
	protected Vector<event>events;
	protected polyhi polyhi;
	protected double time;
	protected double x;
	protected double y;
	protected double agl;
	protected double boundingrad;
	protected double boundingrad2;

	public object(world w,double x,double y,double agl){
		this.wld=w;
		this.x=x;
		this.y=y;
		this.agl=agl;
		events=new Vector<event>(20,10);
		time=0.0;
		isalive=true;
	}

	final protected void boundingradius(final double d){boundingrad=d;boundingrad2=d*d;}
	final public double boundingradius(){return boundingrad;}
	final public double boundingradius2(){return boundingrad2;}

	final boolean collisioncheck(final object o){
		double dx=o.x()-x;
		double dy=o.y()-y;
		if(dx*dx+dy*dy>(boundingrad2+2.0*boundingrad*o.boundingradius()+o.boundingradius2())) return false;
		return true;
	}

	public void update(double d){time+=d;}

	final protected void updateTheOccupiedGrids(){
		polyhi.setStates(x,y,agl);
		final Vector<grid>theOccupiedGrids=new Vector<grid>(4,4);
		wld.grids().getGridsForCircle(x,y,boundingrad,theOccupiedGrids);
		polyhi.transformLocalToWorld();
		for(int i=0;i<theOccupiedGrids.size();i++)
			((grid)theOccupiedGrids.elementAt(i)).add(this);
	}

	final void handleEvents(){
		boolean bool=true;
		int i=events.size();
		for(/**/;i-->0&&bool;bool=handleevent((event)events.elementAt(i))){
			/* empty */
		}
		events.removeAllElements();
	}

	protected boolean handleevent(event e){
		if(e instanceof eventcollision) return handlecollision(((eventcollision)e).object());
		return true;
	}

	public boolean interestedOfCollisionWith(object o){return false;}
	protected boolean handlecollision(object o){return false;}
	final public world world(){return wld;}
	final void addCollision(object o){events.addElement(new eventcollision(o));}
	public void paint(Graphics g,viewport vp){
		if(vp.circleInView(x,y,boundingrad)) polyhi.paint(g,vp);
	}

	final public void polyhi(polyhi psh){
		polyhi=psh;
		updateTheOccupiedGrids();
	}

	final public double x(){return x;}
	final public double y(){return y;}
	final public double angle(){return agl;}
	final public boolean isalive(){return isalive;}
	public void die(){isalive=false;}
	final public void paintBoundingCircle(Graphics g,viewport vp){
		g.setColor(Color.white);
		int i=(int)vp.scaleX(boundingrad*2.0);
		g.drawOval(vp.x(x-boundingrad),vp.y(y+boundingrad),i,i);
	}
}
