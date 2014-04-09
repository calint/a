package a.y.scii;

import b.a;
import b.xwriter;
public class $ extends a{
	public a div;
	public a medid;{medid.set(0);}
	@Override public void to(final xwriter x)throws Throwable{
		x.style("html","background:#f8f8f8");
		name_to(x);
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
		mds.scr.screen_to_outputstream(x.outputstream());
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
		mds.update();
		mds.draw();		
	}
	public void x_reset(xwriter x,String a){
		mds.rst();
		
		final image spi=new image();
		spi.load();
		
		final sprite sp=new sprite();
		sp.image=spi;
		sp.xy(3,2);
		mds.sprites.add(sp);
		
		final sprite sp2=new sprite();
		sp2.image=spi;
		sp2.xy(30,8);
		mds.sprites.add(sp2);
		
		step();
		
		x.xreload();
	}
	public void x_keyb(xwriter x,String key)throws Throwable{
		final int player=medid.toint();
		mds.sprites.get(player).on_msg(key);
		step();
		final xwriter xx=x.xub(div,true,true);
		mds.scr.screen_to_outputstream(xx.outputstream());
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
