package a.civ;

import java.util.List;

import b.a;
import b.xwriter;

final public class listui extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.pl("-- -- - - - -  --- - - - - - - - - - - - -- -  - --  -- - - - - - - ");
		final String id=id();
		ls.stream().forEach(e->{try{
			x.p("<a onkeydown=\"if(event.shiftKey&&event.keyIdentifier=='Down'){").axjs(id,"d",e.nm()).p("}\" href=\"javascript:").axjs(id,""," c "+e.nm()).p("\">").p(" • ").p("</a>");
			e.to(x);
		}catch(Throwable t){throw new Error(t);}});
		x.pl("-- -- - - - -  --- - - - - - - - - - - - -- -  - --  -- - - - - - - ");
	}
	synchronized public void x_(xwriter x,String s){
		x.xalert(s);
	}
	
	/**wire*/public List<? extends a>ls;
}
