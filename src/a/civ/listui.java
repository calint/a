package a.civ;

import java.util.Collections;
import java.util.List;

import b.a;
import b.xwriter;

final public class listui extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.el(this);
		x.pl("-- -- - - - -  --- - - - - - - - - - - - -- -  - --  -- - - - - - - ");
		final String id=id();
		ls.stream().forEach(e->{try{
			x.p("<a id=\""+id+"~"+e.nm()+"\" onkeydown=\"if(event.shiftKey&&event.keyIdentifier=='Down'){").axjs(id,"d",e.nm()).p(";}if(event.shiftKey&&event.keyIdentifier=='Up'){").axjs(id,"u",e.nm()).p(";}\" href=\"javascript:").axjs(id,"c",e.nm()).p("\">").p(" • ").p("</a>");
			e.to(x);
		}catch(Throwable t){throw new Error(t);}});
		x.pl("-- -- - - - -  --- - - - - - - - - - - - -- -  - --  -- - - - - - - ");
		x.el_();
	}
	/**elem click*/
	synchronized public void x_c(xwriter x,String s){
		x.xalert(s);
	}
	/**move elem down*/
	synchronized public void x_d(xwriter x,String s)throws Throwable{
		int c=0;
		for(final a e:ls){
			if(s.equals(e.nm()))break;
			c++;
		}
		int d=c+1;
		if(d==ls.size())d=0;
		Collections.swap(ls,c,d);
		
		$.xto(x,this,this,true,false);
		x.xfocus(id()+"~"+s);
//		ev(x,this);
	}
	/**move elem up*/
	synchronized public void x_u(xwriter x,String s)throws Throwable{
		int c=0;
		for(final a e:ls){
			if(s.equals(e.nm()))break;
			c++;
		}
		int d=c-1;
		if(d==-1)d=ls.size()-1;
		Collections.swap(ls,c,d);
		
		$.xto(x,this,this,true,false);
		x.xfocus(id()+"~"+s);
//		ev(x,this);
	}
	/**wire*/public List<? extends a>ls;
}
