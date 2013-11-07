package d2;
import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;

final public class applet extends Applet implements Runnable{
	static final long serialVersionUID=1;
	public static String clsnm="d2.game.$";
	world wld;
	Thread thd;
	double tm;

	public void init(){try{
		wld=(world)Class.forName(clsnm).newInstance();
		wld.init(getImage(this.getCodeBase(),"d2/background2.jpg"));
	}catch(Throwable t){throw new Error(t);}}

	public void start(){
		thd=new Thread(this,getClass().getName());
//		thd.setPriority(10);
		thd.start();
	}
	public void stop(){thd=null;}
	public void run(){
		int frm=0;
		double dt=0;
		long frmt=System.nanoTime();
		while(thd!=null){
			final long t0=System.nanoTime();
			if(t0-frmt>1000000000){
				frmt=t0;
				getAppletContext().showStatus("fps:"+frm+",upd:"+dt);
				frm=0;
			}
			frm++;
			if(dt==0)
				dt=.0001;
			wld.update(dt);
			update(getGraphics());
//			repaint();
			final long t1=System.nanoTime();
			final long dtns=t1-t0;
			dt=dtns/1000000000.;
		}
	}
	private Image offScreenImage;
	private Graphics offScreenGraphics;
	private Dimension offScreenSize;

	public final void update(Graphics graphics){
		Dimension dimension=getSize();
		if(offScreenImage==null||dimension.width!=offScreenSize.width||dimension.height!=offScreenSize.height){
			offScreenImage=this.createImage(dimension.width,dimension.height);
			offScreenSize=dimension;
			offScreenGraphics=offScreenImage.getGraphics();
			offScreenGraphics.clearRect(0,0,offScreenSize.width,offScreenSize.height);
		}
		wld.paint(offScreenGraphics);
		graphics.drawImage(offScreenImage,0,0,null);
	}
	
	public boolean keyDown(Event event,int i){onkeyb(event.key,true);return true;}
	public boolean keyUp(Event event,int i){onkeyb(event.key,false);return true;}
	private void onkeyb(int key,boolean pressed){wld.onkeyb(key,pressed);}
}
