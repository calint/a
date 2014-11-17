package a.civ;

import java.util.List;

import b.a;
import b.xwriter;

public class listui extends a {
	@Override public void to(xwriter x)throws Throwable{
		ls.forEach(e->{try{e.to(x);}catch(Throwable t){throw new Error(t);}});
	}
	public List<? extends a>ls;
}
