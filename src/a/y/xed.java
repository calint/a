package a.y;
import b.a;
import b.xwriter;
public class xed extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.nl();
		x.p("content editable: ");
//		x.xed(this);
		x.p("  ");
		x.dived(this,this,null);
		x.style(this,"display:inline-block;border:1px dotted green;background:yellow;min-width:8em");
		x.focus(this);
//		x.nl();
		x.spc().ax(this);
	}
	public void x_(final xwriter x,final String s){
		x.xalert(toString());
	}
}
