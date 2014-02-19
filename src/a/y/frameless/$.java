package a.y.frameless;
import java.util.LinkedHashMap;
import java.util.Map;
import b.a;
import b.xwriter;
public class $ extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.p("<div ondrop=\"alert('ondrop')\" style=\"height:100%;border:1px solid red\">");
//		x.pl("frameless");
		for(final window w:windows.values()){
//			x.p("<div id="+w.id()+" onclick=\"$x('"+w.id()+"')\" style=\"border:1px solid black;position:absolute;top:"+w.y+"px;left:"+w.x+"px\">");
			x.p(w);
//			x.p("</div>");
//			x.nl();
		}
		x.p("</div>");
	}
	protected a chldq(final String id){
		return windows.get(id);
	}
	private Map<String,window>windows=new LinkedHashMap<String,window>();
	{	window w;
		w=new window();
		w.x=11;
		w.y=11;
		w.pt(this).nm("1");
		windows.put(w.nm(),w);
		w=new window();
		w.x=21;
		w.y=21;
		w.pt(this).nm("2");
		windows.put(w.nm(),w);
	}
	
	public static class window extends a{
		static final long serialVersionUID=1;
		int x,y;
		public void to(final xwriter x)throws Throwable{
			final String id=id();
			x.p("<el id="+id+" draggable=\"true\" ondragstart=\"$d('drag start')\" ondragend=\"$d('drag end '+this.style.top+' '+event);\" ondrop=\"$d('drag drop')\" onclick=\"$x('"+id+"')\" style=\"border:1px solid black;position:absolute;top:"+y+"px;left:"+this.x+"px\">");
			x.pl("window "+nm());
			x.ax(this,"a");
			x.p("</el>");
		}
		public void x_(final xwriter x,final String s)throws Throwable{
			this.x+=(int)(Math.random()*3);
			this.y+=(int)(Math.random()*3);
			x.pl("var e=$('"+id()+"');e.style.top='"+this.y+"px';e.style.left='"+this.x+"px';");
//			x.xuo(this);
		}
		public void x_a(final xwriter x,final String s)throws Throwable{
			x.xuo(this);
		}
	}
}
