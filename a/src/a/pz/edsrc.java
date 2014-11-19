package a.pz;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import b.a;
import b.xwriter;
final public class edsrc extends a{
	static final long serialVersionUID=11;
	public boolean edit=false;
	int focusline;
	private int lstfocusline=-1;
	public a brkpts;
	private Set<Integer>brkptsset=new HashSet<Integer>();
	void xfocusline(xwriter x){
		if(edit)return;
		if(lstfocusline!=-1)x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lstfocusline-1)+"];e.className=e._oldcls;");
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(focusline-1)+"];e._oldcls=e.className;e.className='stp';");
		lstfocusline=focusline;
	}
	public a src;
	public void to(final xwriter x)throws Throwable{
		x.div(this,"float textleft panel");
		x.p("source");
		x.ax(this,"f1"," edit");
		x.ax(this,"f2"," view");
		if(edit){
			x.nl();
			x.inptxtarea(src);
			x.div_();
			return;
		}
		final StringReader sr=new StringReader(src.toString());
		final BufferedReader br=new BufferedReader(sr);
		x.tag("ol");
		int lno=1;
		final String id=id();
		for(String ln;(ln=br.readLine())!=null;lno++){
			x.tago("li").attr("lno",lno).attr("onclick=\"$x('"+id+" brk '+this.getAttribute('lno'))\"");
			final boolean brk=isonbrkpt(lno);
			if(brk)
				x.attr("class","brk");
			x.tagoe().pl(ln);
		}
		x.tage("ol");
		x.div_();
	}
	public boolean isonbrkpt(final int srclno){
		return brkptsset.contains(srclno);
	}
	synchronized public void x_brk(xwriter x,String s)throws Throwable{
		final int lno=Integer.parseInt(s);
		if(brkptsset.contains(lno)){
			brkptsset.remove(lno);
			brkpts.set(brkptsset.toString());
			x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e.className=e._oldcls;");
			return;
		}
		brkptsset.add(lno);
		brkpts.set(brkptsset.toString());
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e._oldcls=e.className;if(!e._oldcls)e._oldcls='';e.className='brk';");
	}
	synchronized public void x_f1(xwriter x,String s)throws Throwable{
		if(edit)return;
		edit=true;
		x.xuo(this);
		x.xfocus(src);
	}
	synchronized public void x_f2(xwriter x,String s)throws Throwable{
		if(!edit)return;
		edit=false;
		x.xuo(this);
	}
}
