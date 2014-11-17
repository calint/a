package a.civ;

import java.util.List;

import b.a;
import b.xwriter;

final public class listui extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.pl("-- -- - - - -  --- - - - - - - - - - - - -- -  - --  -- - - - - - - ");
		ls.stream().forEach(e->{try{
			x.ax(this," c "+e.nm()," • ");
			e.to(x);
		}catch(Throwable t){throw new Error(t);}});
		x.pl("-- -- - - - -  --- - - - - - - - - - - - -- -  - --  -- - - - - - - ");
	}
	public List<? extends a>ls;
}
