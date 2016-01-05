package a.pzm;

import b.a;
import b.xwriter;

final public class metrics extends a{
	long instr;
	long frames;
	long ldc;
	long stc;
	public void rst(){instr=frames=ldc=stc=0;}
	@Override public void to(xwriter x) throws Throwable{
		x.p(instr).spc().p(frames).spc().p(ldc).spc().p(stc);
	}
	private static final long serialVersionUID=1;
}