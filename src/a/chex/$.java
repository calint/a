package a.chex;

import b.a;
import b.xwriter;

public final class $ extends a{
	public board b;
	{
		b.get_square(0,0).set_piece(new pawn());
	}
	@Override public void to(xwriter x)throws Throwable{
//		x.p(getClass().toString());
		b.to(x);
	}
	private static final long serialVersionUID=1;
}
