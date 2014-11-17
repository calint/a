package a.civ;
import java.util.*;
import java.util.stream.*;
import b.*;
final public class alist<T extends a>extends a{
	private int mode,focus;
	public void mode_view_focus(int ix){mode=1;focus=ix;}
	public void mode_view_all(){mode=0;}
	@Override public void to(xwriter x)throws Throwable{
		x.el(this);
		if(mode==1){
			ls.get(focus).to(x);
			return;
		}
		final String id=id();
		ls.stream().forEach(e->{try{
			x.p("<a id=\""+id+"~"+e.nm()+"\" onkeydown=\"if(!event.shiftKey)return;var i=event.keyIdentifier;if(i!='Down'&&i!='Up'&&i!='Right'&&i!='Left')return;$x('"+id+" '+i+' "+e.nm()+"');\" href=\"javascript:").axjs(id,"c",e.nm()).p("\">").p(" • ").p("</a>");
			e.to(x);
		}catch(Throwable t){throw new Error(t);}});
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
		x.xu(this);
		x.xfocus(id()+"~"+s);
	}
	/**move elem to right list*/
	synchronized public void x_Right(xwriter x,String s)throws Throwable{
		if(rht==null)return;
		final int c=find_elem_index_by_name_or_break(s);
		final T e=ls.remove(c);
		rht.add(e);
		if(x==null)return;
		x.xu(this);
		x.xu(rht);
		rht.xfocus(x,e);
	}
	/**move elem to left list*/
	synchronized public void x_Left(xwriter x,String s)throws Throwable{
		if(lft==null)return;
		final int c=find_elem_index_by_name_or_break(s);
		final T e=ls.remove(c);
		lft.add(e);
		if(x==null)return;
		x.xu(this);
		x.xu(lft);
		lft.xfocus(x,e);
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
	public void link_to_left_of(final alist<T>ls){
		lft=ls.lft;
		ls.lft=this;
		rht=ls;
	}
	public void link_to_right_of(final alist<T>ls){
		rht=ls.rht;
		ls.rht=this;
		lft=ls;
	}
	public void link_warp(final alist<T>ls){
		rht=ls;
		ls.lft=this;
	}
	public int size(){return ls.size();}
	public void clear(){ls.clear();}
//	public T get(int ix){return ls.get(ix);}
	private static final long serialVersionUID=1;
	public T get_first(){if(ls.isEmpty())return null;return ls.get(0);}
}
