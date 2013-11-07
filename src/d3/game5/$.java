package d3.game5;
import d3.applet;
import d3.cfg;
import d3.p3;
import d3.player;
import d3.setup;
import d3.window;
import d3.world;

public class $ implements d3.setup{
	final p3 lookdown=new p3(-Math.PI/2,0,0);
	public void do_setup(applet app,setup.env env) throws Throwable{
		cfg.rend_gnd=false;
		cfg.rend_wire=true;
		cfg.rend_solid=true;
		cfg.rend_wire_pen=true;
		cfg.fixed_dt_ms=20;
		cfg.rend_hud=true;
		cfg.scr_dist=256;
		env.wld=new world(-1024,-1024,2048,8,-4.812);
		env.win=new window(env.wld,app.getSize(),100,env.wld,new p3(0,25,0),lookdown.clone(),8,-1.0,1024.0,10.0,0).cl_spc(0,0,0x40);
		env.plrs=new player[1];
		env.plrs[0]=new recon(env.wld,10,10,0);
		env.wld.player(env.plrs[0]);
		//dots(env.wld,16);
	}
	static void dots(final world w,final int density){
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
