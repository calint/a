package d2;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
public abstract class world{
	static final long serialVersionUID=1L;
	protected grids grids;
	final protected Stack<object>newobjects=new Stack<object>();
	final protected LinkedList<object>objects=new LinkedList<object>();
	protected double time;
	protected viewport viewport;
	protected Dimension dim=new Dimension(512,512);

	public world(double x0,double y0,double wihi,int rows){
		viewport=new viewport(x0,y0,x0+wihi,y0+wihi,dim.width,dim.height,false);
		grids=new grids(x0,y0,wihi,rows);
//		time=0;
		reset();
	}

	public void add(object o){newobjects.push(o);}

	public void update(double dt){
		time+=dt;
		objects.addAll(newobjects);
		newobjects.clear();
		for(final Iterator<object>i=objects.iterator();i.hasNext();){
			final object o=i.next();
			if(o.isalive())
				o.update(dt);
			else
				i.remove();
		}
		grids.detectCollisionsAndClear();
		for(final object o:objects)
			o.handleEvents();
		devineIntervention(dt);
	}

	protected void devineIntervention(double dt){}
	public void paint(Graphics graphics){}
	public void reset(){
		objects.clear();
		newobjects.clear();
		time=0;
	}
	protected void onkeyb(int key,boolean pressed){}
	public grids grids(){return grids;}
	public void init(Image image){}
	public Dimension size(){return dim;}
}
