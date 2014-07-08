package a.pczero;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import b.a;
import b.xwriter;
final public class srcviwr extends a{
	static final long serialVersionUID=11;
	public boolean edit=false;
	int focusline;
	private int lstfocusline=-1;
	public a brkpts;
	private Set<Integer>brkptsset=new HashSet<Integer>();
//	public String libgstep="yellow";
//	public String libgbrkpt="gray";
//	public String libghvr="#aaa";
	void xfocusline(xwriter x){
		if(edit)
			return;
		if(lstfocusline!=-1){
			x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lstfocusline-1)+"];e.className=e._oldcls;");			
		}
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(focusline-1)+"];e._oldcls=e.className;e.className='stp';");
		lstfocusline=focusline;
	}
	public a txt;
	public void to(final xwriter x)throws Throwable{
		x.el(this);
		x.style();
		x.css(this,"ol li.brk","background:green");
		x.css(this,"ol li.stp","background:#c88");
		x.style_();
		x.p("source");
		x.ax(this,"f1"," edit");
		x.ax(this,"f2"," view");
		if(edit){
			x.nl();
			x.style(txt,"outline:none;width:20em;height:100%;min-height:128em;resize:none;border-left:0px dotted #333;color:#444;padding:0 1em 0 .5em;line-height:1.5em;background:#fef");
			x.inputTextArea(txt);
			x.el_();
			return;
		}
		final StringReader sr=new StringReader(txt.toString());
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
//		x.style("ol li:hover","cursor:pointer;background-color:"+libghvr);
		x.el_();
	}
	public boolean isonbrkpt(final int srclno){
		return brkptsset.contains(srclno);
	}
	synchronized public void x_brk(xwriter x,String s)throws Throwable{
		final int lno=Integer.parseInt(s);
		if(brkptsset.contains(lno)){
			brkptsset.remove(lno);
			brkpts.set(brkptsset.toString());
//			x.xu(brkpts);
//			x.xuo(this);
			x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e.className=e._oldcls;");
			return;
		}
		brkptsset.add(lno);
		brkpts.set(brkptsset.toString());
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e._oldcls=e.className;if(!e._oldcls)e._oldcls='';e.className='brk';");

//		x.xu(brkpts);
//		x.xuo(this);
	}
	synchronized public void x_f1(xwriter x,String s)throws Throwable{
		if(edit)return;
		edit=true;
		x.xuo(this);
		x.xfocus(txt);
	}
	synchronized public void x_f2(xwriter x,String s)throws Throwable{
		if(!edit)return;
		edit=false;
		x.xuo(this);
	}
}
