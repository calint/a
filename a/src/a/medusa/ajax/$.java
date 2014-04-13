package a.medusa.ajax;

import java.nio.ByteBuffer;

import a.medusa.medusa;
import a.medusa.player;
import a.medusa.screen;
import a.medusa.algebra.plane;
import a.medusa.algebra.planes;
import a.medusa.algebra.point;
import a.medusa.level.sprite;
import b.a;
import b.xwriter;
public class $ extends a{
	public a div;
	public a medid;{medid.set(0);}
	private screen scr=new screen(80,40);
	@Override public void to(final xwriter x)throws Throwable{
		x.title("medusa ascii game - ajax");
		x.style("html","background:#f8f8f8");
		name_to(x);
		
		
		////// 
//		x.nl();
//		final point p0=new point(1,0,0);
//		p0.to(x);
//		final point n0=new point(1,0,0);
//		final plane pn0=new plane(p0,n0,false);
//		x.nl();pn0.to(x);
//		final plane pn1=pn0;
//		final plane pn2=pn0;
//		final plane pn3=pn0;
//		
//		final planes pns=new planes().planes_add(pn0).planes_add(pn1).planes_add(pn2).planes_add(pn3);
//		x.nl();pns.to(x);
		
//		final planes pns_sqr=new planes(
//			new plane(new point( 1, 0,0),new point( 1, 0,0),false),//? right plane
//			new plane(new point(-1, 0,0),new point(-1, 0,0),false),//? left plane
//			new plane(new point( 0, 1,0),new point( 0, 1,0),false),//? top plane
//			new plane(new point( 0,-1,0),new point( 0,-1,0),false)//? bottom plane
//		);
//		pns_sqr.to(x);
		
		//////
		x.nl();
		x.style(medid,"border:1px dotted green;width:1em;text-align:right;margin:3px;background:green;color:white");
		x.p("   ").ax(this,"reset",":: reset").p("   use keys wesd to move medusa -> ").inputText(medid).nl().nl();
		x.script().nl();
		x.pl("ui.keys['W']=\"$x('_ keyb w')\"");
		x.pl("ui.keys['A']=\"$x('_ keyb a')\"");
		x.pl("ui.keys['S']=\"$x('_ keyb s')\"");
		x.pl("ui.keys['D']=\"$x('_ keyb d')\"");
		x.pl("ui.keys['E']=\"$x('_ keyb e')\"");
		x.pl("document.focus();");
		x.scriptEnd();
		x.pl("- - - - - -  - - - - -- - - - - - - - - - -- - - - -- - - - - - -- - - -- - --");
		x.style(div,"display:block;margin:auto;border:1px dotted white;");
		x.el(div);
		scr.screen_to_outputstream(x.outputstream());
		x.elend();
		x.pl("- - - - - -  - - - - -- - - - - - - - - - -- - - - -- - - - - - -- - - -- - --");
	}
	private void name_to(xwriter x){
		final CharSequence s="medusa ascii game";
		final int size=s.length();
		for(int i=0;i<size;i++){
			final char ch=s.charAt(i);
			final float r=(float)Math.random()+2.5f;
			final int c=(int)(Math.random()*0xffffff);
			final String chex=Integer.toHexString(c);
			x.p("<span style='font-size:"+r+"em;color:#"+chex+"'>").p(ch).p("</span>");
		}
	}
	public void step(){
		final float dt=1;
		mds.tick(dt,mds);
		mds.draw(scr,mds);		
	}
	public void x_reset(xwriter x,String a)throws Throwable{
		mds.reset();
		
		final sprite spi=new sprite();
		spi.load();
		
		final player sp=new player();
		sp.glo(spi);
		sp.xy(3,2);
		mds.players_add(sp);
		
		final player sp2=new player();
		sp2.glo(spi);
		sp2.xy(30,8);
		mds.players_add(sp2);
		
		step();
		
		x.xreload();
	}
	public void x_keyb(xwriter x,String key)throws Throwable{
		final int player=medid.toint();
		mds.players_get(player).on_msg(ByteBuffer.wrap(key.getBytes()),mds);
		step();
		final xwriter xx=x.xub(div,true,true);
		scr.screen_to_outputstream(xx.outputstream());
		x.xube();
//		x.xreload();
	}
	static medusa mds=new medusa();
//	static Thread medusa_thread=new Thread(new Runnable(){@Override public void run(){
//		while(on){
//			mds.update();
//			mds.draw();
//			try{Thread.sleep(1000);}catch(InterruptedException ignored){}
//		}
//	}public boolean on=true;},"medusa");static{mds.rst();medusa_thread.start();}
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
}
