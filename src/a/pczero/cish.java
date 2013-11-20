package a.pczero;
import b.a;
import b.xwriter;
public class cish extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
		x.style().css("html","font-size:13em").styleEnd();
		x.pre().pl(getClass().toString()).nl();
		x.ol();
		x.li().p("operating system : name    8B @   0x0000");
		x.li().p("clock            : ticks   8B @   0x0008");
		x.li().p("loop             : counter 8B @   0x0010");
		x.li().p("keyboard, 128    : keyb  128b @   0x0018");
		x.li().p("free             : free   d0h @   0x0020");
		x.li().p("code             : doio       @   0x0100");
		x.li().p("input/output     : io     32K @   0x1000");
		x.li().p("out of mem       : oom        @ 0x1:0000");
		x.li();
		x.li().p("loop");
		x.li().p("  if keyb hasbit 1");
		x.li().p("     io 'hello 'counter' from 'name");
		x.li().p("     doio.");
		x.li().p("  counter inc");
		x.ol_();
	}
}