package a.chex;

import b.a;
import b.xwriter;

public final class piece extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.p(getClass().toString());
	}
	private static final long serialVersionUID=1;
}
