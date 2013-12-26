package a.budget;
import java.util.Date;

import b.*;
import static b.b.*;
public class log extends a{
	static final long serialVersionUID=1;
	public a s;//item
	public a q;{q.set("1");}//qty
	public a d;{d.set(new Date().toString());}//date
	public a l;//output
	final public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("html","font-size:2em;padding-left:4em;line-height:1.5em");
		x.css(s,"border:1px dotted green;padding:.5em");
		x.css(q,"width:4em;align:right;border:1px dotted green;padding:.5em");
		x.css(d,"border:1px dotted green;padding:.5em;width:20em");
		x.css("hr","color:black;height:.5em");
		x.styleEnd();
		x.pl(getClass().toString());
		x.nl();
		x.inputText(s,null,this,"s").nl();
		x.inputText(q,null,this,"s").nl();
		x.inputText(d,null,this,"s").nl();
		x.nl().nl();
		x.ax(this,"li"," day");
		x.ax(this,"li"," week");
		x.ax(this,"li"," month");
		x.ax(this,"li"," year");
		x.hr();
		x.el(l);
		try{path().to(x);}catch(final Throwable t){x.pl(stacktraceline(t));}
		x.elend();
	}
	private path path(){
		return req.get().session().path(getClass().getPackage().getName()).get("log");
	}
	public void ax_s(final xwriter x,final String[]p)throws Throwable{
		path().append(d+" "+q+" "+s,"\n");
		path().to(x.xub(l,true,true));x.xube();
		x.xu(s.clr());
		x.xu(d.set(new Date().toString()));
		x.xu(q.set("1"));
		x.xfocus(s);
	}
}
