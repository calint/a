package a.frameless;

import b.a;
import b.xwriter;

public class $ extends a {
	@Override
	public void to(xwriter x) throws Throwable {
		b.b.cp(getClass().getResourceAsStream("index.html"),x.outputstream());
	}
}
