package a.chex;

import b.a;
import b.xwriter;

public final class square extends a{
	public piece p;
	@Override public void to(xwriter x)throws Throwable{
		x.p(getClass().toString());
		p.to(x);
	}
	private static final long serialVersionUID=1;
}
