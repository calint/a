package d3;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.NumberFormat;
public final class applet extends Applet implements Runnable{
	static boolean debug=true;
	private static final long serialVersionUID=1L;
	private final static String zeros="0000000000";
	private final static String fps(double fps){
		return num((int)Math.floor(fps),3);
	}
	private final static String num(long n,int size){
		String s=Long.toString(n);
		int len=size-s.length();
		if(len>0)
			s=zeros.substring(0,len)+s;
		return s;
	}
	public final static String sdbl(double d){
		return NumberFormat.getNumberInstance().format(d);
	}
	private boolean alive;
	private Dimension buf_dim;
	private Graphics buf_g;
	private Image buf_img;
	private Dimension dim;
	private double dt;
	private long dt_ms;
	private float fps;
	private int fps_f;
	private long fps_t;
	private int frame;
	private int mouse_btn;
	private int mouse_x;
	private int mouse_y;
	private player plrprv;
	private String pkg;
	private boolean slowmo;
	private double slowmot;
	private long t_ms;
	private Thread thd;
	private long tprv_ms;
	//	private window window;
	//	private world world;
	private double zm;
	private setup.env env;
	private int nplayers;
	private byte[]keys;
	public final void init(){
		final String s=getParameter("setup");
		if(s!=null&&s.length()!=0)
			pkg=s;
		else
			pkg=cfg.pkg;
		try{
			final setup stp=(setup)Class.forName("d3."+pkg+".$").newInstance();
			env=new setup.env();
			stp.do_setup(this,env);
			nplayers=env.plrs.length;
		}catch(Throwable e){
			throw new Error(e);
		}
		zm=100.0;
		keys=new byte[net.protocol_msg_len];
		enableEvents(KeyEvent.KEY_EVENT_MASK|MouseEvent.MOUSE_EVENT_MASK|MouseEvent.MOUSE_MOTION_EVENT_MASK|MouseEvent.MOUSE_WHEEL_EVENT_MASK);
		validate();
		final String s2=getParameter("nplayers");
		if(s2!=null)
			nplayers=Integer.parseInt(s2);
	}
	private void keyb(final int key,final boolean prsd){
//		System.out.println(key);
		byte v=(byte)(prsd?127:0);
		switch(key){
		case 'z':env.win.clp_znear+=zm*dt;break;
		case 'Z':env.win.clp_znear-=zm*dt;break;
		case 'q':env.win.scr_dst+=zm*dt;break;
		case 'Q':env.win.scr_dst-=zm*dt;break;
		case 'f':cfg.rend_wire=true;break;
		case 'F':cfg.rend_wire=false;break;
		case 's':slowmo=true;break;
		case 'S':slowmo=false;break;
		case 'd':cfg.rend_wire_pen=true;break;
		case 'D':cfg.rend_wire_pen=false;break;
		case 'e':cfg.rend_gnd=false;break;
		case 'E':cfg.rend_gnd=true;break;
		case ',':cfg.rend_hud=true;break;
		case '.':cfg.rend_hud=false;break;
		case 'k':keys[1]=v;break;
		case 'K':keys[1]=(byte)(v>>4);break;
		case 'h':keys[1]=(byte)-v;break;
		case 'H':keys[1]=(byte)-(v>>4);break;
		case ' ':
		case 't':keys[2]=v;break;
		case 'g':keys[3]=v;break;
		case 'y':keys[4]=v;break;
		case 'i':keys[5]=v;break;
		case 'u':keys[6]=(byte)(v>>4);break;
		case 'U':keys[6]=v;break;
		case 'j':keys[6]=(byte)-v;break;
		case 'J':keys[6]=(byte)-(v>>4);break;
		case 'a':keys[0]=v;break;
		case '1':keys[7]=1;break;
		case '2':keys[7]=2;break;
		case '3':keys[7]=3;break;
		case '§':keys[8]=1;break;
		case '½':keys[9]=1;break;
		}
	}
	private void metrics_clear(){
		cfg.rend_wire=Math.random()>0.5;
		metrics.coldet[0]=0;
		metrics.coldethit[0]=0;
		metrics.obj_rend_cull_proj=0;
		metrics.rend_noclp=0;
		metrics.obj_rend_clp=0;
		metrics.obj_rend_clpno=0;
		metrics.cam_front=0;
		metrics.cam_cull_front=0;
		metrics.cam_clip_front=0;
		metrics.cam_left=0;
		metrics.cam_cull_left=0;
		metrics.cam_clip_left=0;
		metrics.cam_right=0;
		metrics.cam_cull_right=0;
		metrics.cam_clip_right=0;
		metrics.cam_top=0;
		metrics.cam_cull_top=0;
		metrics.cam_clip_top=0;
		metrics.cam_btm=0;
		metrics.cam_cull_btm=0;
		metrics.cam_clip_btm=0;
		metrics.rend_npoly=0;
		metrics.grd_put=0;
		metrics.grd_take=0;
		metrics.rend_poly_clpno=0;
		for(int k=0;k<metrics.rend_poly_clp.length;k++){
			metrics.rend_poly_clp[k]=0;
			metrics.rend_poly_clp_cull[k]=0;
		}
		metrics.rend_poly_clp_cullcf=0;
	}
	private final void paint_hud(final Graphics g){
		g.setColor(Color.white);
		int dy=11;
		int y=dim.height>>1;
		int x=21;
		player pl=env.wld.player();
		g.drawString("frame:"+num(frame,7)+"  player:"+playerix+"  dtms:"+num(dt_ms,2)+"  fps: "+fps(fps)+"   rpgs: "+cfg.cnt_rpg+"  bullets: "+cfg.cnt_bullet+":   frags: "+cfg.cnt_frag+(pl==null?"":("  life:"+pl.health())),x,y+=dy);
		g.drawString("objects#="+num(metrics.cam_objq,7),x,y+=dy);
		g.drawString(" grid(put,take)="+num(metrics.grd_put,3)+"  "+num(metrics.grd_take,3),x,y+=dy);
		g.drawString(" collision(try,hit)="+num(metrics.coldet[0],3)+"  "+num(metrics.coldethit[0],3),x,y+=dy);
		g.drawString(" front(cull,clp,in)="+num(metrics.cam_cull_front,5)+"  "+metrics.cam_clip_front+"  "+metrics.cam_front,x,y+=dy);
		g.drawString("  left(cull,clp,in)="+num(metrics.cam_cull_left,5)+"  "+metrics.cam_clip_left+"  "+metrics.cam_left,x,y+=dy);
		g.drawString("   right(cull,clp,in)="+num(metrics.cam_cull_right,5)+"  "+metrics.cam_clip_right+"  "+metrics.cam_right,x,y+=dy);
		g.drawString("    top(cull,clp,in)="+num(metrics.cam_cull_top,5)+"  "+metrics.cam_clip_top+"  "+metrics.cam_top,x,y+=dy);
		g.drawString("     btm(cull,clp,in)="+num(metrics.cam_cull_btm,5)+"  "+metrics.cam_clip_btm+"  "+metrics.cam_btm,x,y+=dy);
		g.drawString("      proj(cull,noclp,clp,clpno,scr)="+num(metrics.obj_rend_cull_proj,5)+"  "+num(metrics.rend_noclp,5)+"  "+num(metrics.obj_rend_clp,5)+"  "+num(metrics.obj_rend_clpno,5)+"  "+num(metrics.cam_rendtot,5),x,y+=dy);
		g.drawString("cam(cull,sort,drw)ms="+num(metrics.cam_cull_ms,2)+"  "+num(metrics.cam_sort_ms,2)+"  "+num(metrics.cam_rend_ms,2),x,y+=dy);
		g.drawString(" skyg(sky,gnd,obj)ms="+num(metrics.cam_sky_ms,2)+"  "+num(metrics.cam_gnd_ms,2)+"  "+num(metrics.cam_obj_ms,2),x,y+=dy);
		g.drawString("wld(coldet,colhdl,upd,grd,drw,net)ms="+num(metrics.wld_coldet_ms,2)+"  "+num(metrics.wld_colhdl_ms,2)+"  "+num(metrics.wld_upd_ms,2)+"  "+num(metrics.wld_updgrds_ms,2)+"  "+num(metrics.wld_paint_ms,2)+"  "+num(metrics.net_rec_dt+metrics.net_send_dt,2),x,y+=dy);
		g.drawString("poly(#,cullcf,clpno)="+metrics.rend_npoly+"  "+metrics.rend_poly_clp_cullcf+"  "+metrics.rend_poly_clpno,x,y+=dy);
		g.drawString("clplft(#,cull)="+metrics.rend_poly_clp[0]+"  "+metrics.rend_poly_clp_cull[0],x,y+=dy);
		g.drawString("clprht(#,cull)="+metrics.rend_poly_clp[1]+"  "+metrics.rend_poly_clp_cull[1],x,y+=dy);
		g.drawString("slowmo(t)="+sdbl(slowmot),x,y+=dy);
		g.drawString("scr(dist,znear)="+sdbl(env.win.scr_dst)+","+sdbl(env.win.clp_znear),x,y+=dy);
		g.drawString("mouse(x,y,btn)="+mouse_x+","+mouse_y+","+mouse_btn,x,y+=dy);
		g.drawString("net(snd,rec)="+num(metrics.net_rec_dt,2)+","+num(metrics.net_send_dt,2),x,y+=dy);
		for(int k=0;k<env.plrs.length;k++){
			obj obj=(obj)env.plrs[k];
			g.drawString(obj.pos()+"·"+obj.lookvec(),x,y+=dy);
		}
	}
	protected final void processEvent(final AWTEvent e){
		if(e instanceof MouseEvent){
			final MouseEvent ev=(MouseEvent)e;
			mouse_x=ev.getX();
			mouse_y=ev.getY();
			mouse_btn=ev.getButton();
			return;
		}
		if(e instanceof KeyEvent){
			final KeyEvent ev=(KeyEvent)e;
			if(ev.getID()==KeyEvent.KEY_RELEASED)
				keyb(ev.getKeyChar(),false);
			else if(ev.getID()==KeyEvent.KEY_PRESSED)
				keyb(ev.getKeyChar(),true);
		}
	}
	private int playerix;
	public final void run(){
		final Socket sock;
		OutputStream os=null;
		InputStream is=null;
		if(nplayers>1){
			final String host=getCodeBase().getHost();
			try{
				sock=new Socket(host,Integer.parseInt(cfg.net_server_port));
				os=sock.getOutputStream();
				is=sock.getInputStream();
				mkplayerid();
				os.write(playerid.getBytes());
				os.flush();
				for(int n=0;n<env.plrs.length;n++){
					final int c=is.read(env.plrs[n].keys());
					if(c==-1)
						throw new IOException("connection to server lost");
				}
				playerix=-1;
				for(int n=0;n<env.plrs.length;n++){
					final String pid=new String(env.plrs[n].keys());
					if(pid.equals(playerid))
						playerix=n;
				}
				env.wld.player(env.plrs[playerix]);
				for(int k=0;k<env.plrs.length;k++){
					final byte[]keys=env.plrs[k].keys();
					for(int n=0;n<keys.length;n++)
						keys[n]=0;
				}
			}catch(NumberFormatException e){
				e.printStackTrace();
			}catch(UnknownHostException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		while(alive){
			final long t0=System.currentTimeMillis();
			metrics_clear();
			final Graphics g=getGraphics();
			if(g==null)
				continue;
			dim=getSize();
			if(dim==null||dim.width==0||dim.height==0)
				continue;
			if(buf_img==null||dim.width!=buf_dim.width||dim.height!=buf_dim.height){
				buf_img=createImage(dim.width*2,dim.height);
				buf_g=buf_img.getGraphics();
				buf_dim=dim;
				env.win.set_dimension(dim);
			}
			final long t1=System.currentTimeMillis();
			if(nplayers>1)try{
				System.arraycopy(keys,0,env.plrs[playerix].keys(),0,keys.length);
				os.write(env.plrs[playerix].keys());
			}catch(IOException e){throw new Error(e);}
			
			final long t2=System.currentTimeMillis();
			metrics.net_send_dt=(int)(t2-t1);
			env.win.paint(buf_g);
			
			long t3=System.currentTimeMillis();
			metrics.wld_paint_ms=(int)(t3-t2);
			frame++;
			t_ms=System.currentTimeMillis();
			if(tprv_ms==0)
				tprv_ms=t_ms;
			dt_ms=t_ms-tprv_ms;
			tprv_ms=t_ms;
			if(dt_ms==0){
				dt_ms=1;
			}
			fps_t+=dt_ms;
			tprv_ms=t_ms;
			dt=dt_ms/1000.0;
			player plr=env.wld.player();
			if(plr!=plrprv){
				slowmo=true;
				slowmot=env.wld.rand(0.3,0.9);
				plrprv=plr;
				env.win.track_object((obj)plr);
			}
			if(slowmo){
				slowmot-=dt;
				dt_ms=1;
				dt=0.001;
				if(slowmot<0)
					slowmo=false;
			}else if(cfg.fixed_dt_ms!=0){
				dt_ms=cfg.fixed_dt_ms;
				dt=(double)dt_ms/1000.0;
			}
			final long t4=System.currentTimeMillis();
			if(nplayers>1){
				for(int n=0;n<env.plrs.length;n++){
					try{
						final int c=is.read(env.plrs[n].keys());
						if(c!=net.protocol_msg_len)
							throw new IOException("expected 32 B. received:"+c);
					}catch(IOException e){
						e.printStackTrace();
					}
//					System.out.println(new String(env.plrs[n].keys()));
				}
			}else
				System.arraycopy(keys,0,env.plrs[0].keys(),0,keys.length);
			long t5=System.currentTimeMillis();
			metrics.net_rec_dt=(int)(t5-t4);
//			System.out.println(" dt: "+dt);
			env.wld.update(dt);
			if(cfg.rend_hud)
				paint_hud(buf_g);
			g.drawImage(buf_img,0,0,null);
			fps_f++;
			if(fps_t>1000){
				fps=1000.0f*fps_f/fps_t;
				fps_t=0;
				fps_f=0;
			}
			long t6=System.currentTimeMillis();
			long sleep=t6-t0;
			sleep=cfg.fixed_dt_ms-sleep;
//			System.out.println(sleep);
			if(sleep>0)
				try{Thread.sleep(sleep);}catch(InterruptedException e){}
		}
	}
	private String playerid;
	private void mkplayerid(){
		String chars="01234567890abcdef";
		StringBuilder sb=new StringBuilder(net.protocol_msg_len);
		for(int n=0;n<net.protocol_msg_len;n++){
			int hx=(int)(Math.random()*16);
			sb.append(chars.charAt(hx));
		}
		playerid=sb.toString();
	}
	public final void start(){
		alive=true;
		thd=new Thread(this);
		thd.start();
	}
	public final void stop(){
		alive=false;
		if(thd==null)
			return;
		thd.interrupt();
		thd=null;
	}
}
