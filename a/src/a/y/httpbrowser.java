package a.y;
import b.a;
import b.xwriter;
public class httpbrowser extends a{static final long serialVersionUID=1;
	public a url;
	public a output;
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css(url,"border:1px solid blue;width:100%");
		x.css(output,"border:1px solid red");
		x.style_();
		x.pre();
		x.inputText(url,null,this,"a").nl();
		x.output(output);
	}
	public void x_a(final xwriter x,final String p)throws Throwable{
		x.xalert(url.toString());
		x.xu(output,url.toString());
	}
	
	public final static class browser{
		public void foo()throws Throwable{
		}
	}
}