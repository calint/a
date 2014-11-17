package a.civ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import b.a;
import b.xwriter;

final public class alist<T extends a>extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.el(this);
//		x.pl("- -- - - - -  --- - - -");
		final String id=id();
		ls.stream().forEach(o->{try{
			final a e=(a)o;
			x.p("<a id=\""+id+"~"+e.nm()+"\" onkeydown=\"if(!event.shiftKey)return;var i=event.keyIdentifier;if(i!='Down'&&i!='Up'&&i!='Right'&&i!='Left')return;$x('"+id+" '+i+' "+e.nm()+"');\" href=\"javascript:").axjs(id,"c",e.nm()).p("\">").p(" • ").p("</a>");
			e.to(x);
		}catch(Throwable t){throw new Error(t);}});
//		x.pl("- -- - - - -  --- - - -");
		x.el_();
	}
	private int ix;
	synchronized public void add(T e){
		e.pt(this);//? ondetach
		e.nm(Integer.toString(ix++));
		ls.add(e);
	}
	public Stream<T>stream(){return ls.stream();}
	/**elem click*/
	synchronized public void x_c(xwriter x,String s){
		x.xalert(s);
	}
	/**move elem down*/
	synchronized public void x_Down(xwriter x,String s)throws Throwable{
		final int c=find_elem_index_by_name_or_break(s);
		int d=c+1;
		if(d==ls.size())d=0;
		swp(x,s,c,d);
//		ev(x,this);
	}
	/**move elem up*/
	synchronized public void x_Up(xwriter x,String s)throws Throwable{
		final int c=find_elem_index_by_name_or_break(s);
		int d=c-1;
		if(d==-1)d=ls.size()-1;
		swp(x,s,c,d);
	}
	private void swp(xwriter x,String s,int c,int d) throws Throwable {
		Collections.swap(ls,c,d);
		if(x==null)return;
		$.xto(x,this,this,true,false);
		x.xfocus(id()+"~"+s);
	}
	/**move elem to right list*/
	synchronized public void x_Right(xwriter x,String s)throws Throwable{
		if(rht==null)return;
		final int c=find_elem_index_by_name_or_break(s);
		final a e=ls.remove(c);
		rht.add((T)e);
		if(x==null)return;
		$.xto(x,this,this,true,false);
		$.xto(x,rht,rht,true,false);
		rht.xfocus(x,(T)e);
	}
	/**move elem to left list*/
	synchronized public void x_Left(xwriter x,String s)throws Throwable{
		if(lft==null)return;
		final int c=find_elem_index_by_name_or_break(s);
		final a e=ls.remove(c);
		lft.add((T)e);
		if(x==null)return;
		$.xto(x,this,this,true,false);
		$.xto(x,lft,lft,true,false);
		lft.xfocus(x,(T)e);
	}
	public void xfocus(xwriter x,T e){x.xfocus(id()+"~"+e.nm());}
	public void xfocus(xwriter x,String elem_name){x.xfocus(id()+"~"+elem_name);}
	private int find_elem_index_by_name_or_break(final String name){
		int c=0;
		for(final a e:ls){
			if(name.equals(e.nm()))return c;
			c++;
		}
		throw new Error();
	}
	private alist<T>lft;
	private List<T>ls=new ArrayList<>();
	private alist<T>rht;
	
	@Override protected a chldq(String nm){
		final a e=ls.stream()
			.filter(ee->nm.equals(ee.nm()))
			.findAny()
			.get();
		if(e!=null)return e;
		return super.chldq(nm);
	}
	
	public static void link(final alist lft,final alist rht){lft.rht=rht;rht.lft=lft;}
	
	private static final long serialVersionUID = 1L;
}
