package a;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import b.a;
import b.xwriter;

public final class coki3d extends a{
	static final long serialVersionUID=1;

	public static long sleepms=1000/10;
	private static Random rnd=new Random(0);
	private static double rnd(){return rnd.nextDouble();}

	public a d0;
	public a d1;
	public a s;
	private int frm;
	private char[][]p0=new char[32][64];
	private char[][]p1=new char[32][64];
	private fpsx fpsx=new fpsx();
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("body","text-align:center;border:0;padding:0;background-color:black");
		x.css("pre.d","margin-top:77px;vertical-align:middle;text-align:center;background:#eeffee");
		x.css("table.t","margin-left:auto;margin-right:auto;background:#eeeeff");
		x.css("table.t td","color:red");
		x.css("table.t td.l","color:#880000;border-left:3px dotted red;border-right:3px dotted red");
		x.css("table.t td.r","color:#006600;border-right:3px dotted red");
		x.css(s,"font-size:8px;color:white");
		x.style_();
		x.pre("d").table("t").tr().td("l").output_holder(d0).td("r").output_holder(d1).table_().preEnd().output_holder(s);
		x.script().p("setTimeout(\"$x('"+id()+" a')\",0)").script_();
	}
	public void x_a(final xwriter x,final String st)throws Throwable{
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:MM:ss.SSS");
		while(true){			
			xwriter y=x.xub(d0,true,false);
			for(int r=0;r<p0.length;r++){
				for(int c=0;c<p0[0].length;c++){
					char ch=p0[r][c];
					final double rr=rnd();
					if(rr>0.55)ch='·';
					if(rr>0.66)ch='.';
					if(rr>0.90)ch=',';
					if(rr>0.95)ch='•';
					y.p(ch==0?' ':ch);
				}
				y.nl();
			}
			x.xube();

			y=x.xub(d1,true,false);
			for(int r=0;r<p1.length;r++){
				for(int c=0;c<p1[0].length;c++){
					char ch=p1[r][c];
					final double rr=rnd();
					if(rr>0.55)ch='·';
					if(rr>0.66)ch='.';
					if(rr>0.90)ch=',';
					if(rr>0.95)ch='•';
					y.p(ch==0?' ':ch);
				}
				y.nl();
			}
			x.xube();

			y=x.xub(s,true,false);
			y.p(sdf.format(new Date())).p("  frame(").p(frm++).p(")   fps(").p(fpsx.get()).p(")");
			x.xube();
			x.flush();
			fpsx.inc();
			fpsx.tick(System.currentTimeMillis());
			Thread.sleep(sleepms);
		}
	}
	public static final class fpsx{
		public int evryms=1000;
		private int c;
		private long t0;
		private int fps;
		public void inc(){c++;}
		public void tick(final long t){
			final long dt=t-t0;
			if(dt<evryms)
				return;
			if(dt==0)
				return;
			fps=(int)Math.round(1000f*c/dt);
			c=0;
			t0=t;
		}
		public int get(){return fps;}
	}
}
