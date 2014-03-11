package d3.game4;
import d3.applet;
import d3.cfg;
import d3.p3;
import d3.player;
import d3.setup;
import d3.window;
import d3.world;
public class $ implements d3.setup{
	public void do_setup(applet app,setup.env env) throws Throwable{
		world wld=new world(-1024,-1024,2048,8,-4.812);
		env.wld=wld;
		mep.mk("/d3/game4/1.mep",wld,0,0,1);
		cubes(wld,8);
		cfg.rend_gnd=false;
		cfg.rend_wire=true;
		cfg.rend_solid=true;
		cfg.rend_wire_pen=true;
		cfg.fixed_dt_ms=50;
		window win=new window(wld,app.getSize(),100,wld,new p3(0,100,-20),new p3(-Math.PI/2,0,0),2,-1.0,1024.0,10.0,0).cl_spc(0,0,0);
		env.win=win;
		env.plrs=new player[2];
		env.plrs[0]=new recon(wld,10,10,0);
		env.plrs[1]=new recon(wld,10,-10,0);
	}
	private void cubes(final world w,final int density){
		final p3 p=new p3(w.xmin(),0,w.ymin());
		final p3 lp=p.clone();
		final double s=w.grdsz()/density;
		final int nsq=w.rows()*density;
		for(int yy=0;yy<nsq;yy++){
			for(int xx=0;xx<nsq;xx++){
				new cube(w,p.clone(),new p3(1));
				p.x+=s;
			}
			lp.z+=s;
			lp.x=w.xmin();
			p.x=lp.x;
			p.z=lp.z;
		}
	}
}
