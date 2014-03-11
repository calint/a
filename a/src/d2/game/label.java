package d2.game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import d2.polyh;
import d2.polyhi;
import d2.viewport;
import d2.world;

class label extends aobjectm{
	protected String myText;
	protected Color myColor;
	protected Font myFont;
	protected double myLifeTime;
	protected polyh ourPolygonSolid;

	public label(world w,double x,double y,double dx,double dy,String string,Font font,Color color,double lifetime){
		super(w,x,y,0,dx,dy,0);
		if(ourPolygonSolid==null) makePolygonSolid();
		this.polyhi(new polyhi(ourPolygonSolid));
		myText=string;
		myColor=color;
		myFont=font;
		myLifeTime=lifetime;
	}

	protected void makePolygonSolid(){
		ourPolygonSolid=new polyh(null,null,null);
	}

	public void paint(Graphics g,viewport vp){
		g.setFont(myFont);
		g.setColor(myColor);
		g.drawString(myText,vp.x(x),vp.y(y));
	}

	public void update(double d){
		super.update(d);
		if(time>myLifeTime) this.die();
	}
}
