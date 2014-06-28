package a.any;

import b.a;
import b.xwriter;

public class sts_ajax extends a{
	@Override public void to(final xwriter x)throws Throwable{
		x.p("<div onclick=\"console.log(event);var e=$('"+id()+"');console.log(e);e.style.cssText='"+sts_css_closed+"';\">");
		x.span(this,sts_css_opened);
		x.divEnd();
	}
	
	private static final String sts_css_opened="transition-duration:1s;transition-timing-function:ease;padding:1em;width:20em;height:1em;background:#fed;box-shadow:0 0 .5em rgba(0,0,0,.5);";
	private static final String sts_css_closed="transition-duration:1s;transition-timing-function:ease;padding:1em;width:20em;height:1em;color:rgba(255,255,255,.5);box-shadow:0 0 0 rgba(0,0,0,0);";

	private static final long serialVersionUID=1L;
}
