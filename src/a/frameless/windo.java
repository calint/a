package a.frameless;

import b.a;
import b.xwriter;

public class windo extends a {
	@Override
	public void to(xwriter x) throws Throwable {
		final String id=id();
		x.p("<div style='position:absolute;border:1px dotted blue;width:20em;height:20em;left:100px;top:130px' id="+id+" draggable=true ondragstart=\"console.log(event);this._x=event.pageX;this._y=event.pageY;console.log(this._x+','+this._y)\" ondragend=\"console.log(event);if(style.left=='')style.left='0px';style.left=(parseInt(style.left.replace('px',''),10)+(event.pageX-this._x))+'px';if(style.top=='')style.top='0px';style.top=(parseInt(style.top.replace('px',''),10)+(event.pageY-this._y))+'px';console.log(style.left+','+style.top)\">");
		x.p("window ").p(id);
		x.pl("</div>");
	}
}
