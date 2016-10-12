package a.chex;

import b.a;
import b.xwriter;

public final class board extends a{
	public square s;
	@Override public void to(xwriter x)throws Throwable{
		x.p(getClass().toString());
		s.to(x);
	}
	private static final long serialVersionUID=1;
}
