package a.pczero;
import b.a;
import b.xwriter;
public class cish extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
		x.style().css("html","font-size:13em").style_();
		x.pre().pl(getClass().toString()).nl();
		x.ol();
		x.li().p("operating system : name    8B @   0x0000");
		x.li().p("clock            : ticks   8B @   0x0008");
		x.li().p("loop             : counter 8B @   0x0010");
		x.li().p("keyboard, 128    : keyb  128b @   0x0018");
		x.li().p("                 :       224B @   0x0020");
		x.li().p("code             : doio ff00h @   0x0100");
		x.li().p("input/output     : io     32K @   0x1000");
		x.li().p("free mem         : mem        @ 0x1:0000");
		x.li();
		x.li().p("loop");
		x.li().p("  if keyb hasbit 1");
		x.li().p("     io 'hello 'counter' from 'name");
		x.li().p("     doio.");
		x.li().p("  counter inc");
		x.ol_();
	}
	final static class cishwriter{
		private final xwriter x;
		private int indent=0;
		public cishwriter(final xwriter x){this.x=x;}
		public xwriter xwriter(){return	 x;}
		public cishwriter loop(){x.li().spc(indent*2).p("loop");indent++;return this;}
		public cishwriter loopx(){indent--;return this;}
		public cishwriter mem(final String fullname,final String shortname,final int size_B,final int addr){return this;}
	}
}