package a.qa;
import b.*;
final public class report extends a{static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.style().css("html","margin:1em 8em 4em 8em").styleEnd();
		x.tago("img").attr("src","/qa/report/bench1.jpg").tagoe().nl();
		x.tago("img").attr("src","/qa/report/bench2.jpg").tagoe().nl();
		x.tago("img").attr("src","/qa/report/bench3.jpg").tagoe().nl();
		x.tago("img").attr("src","/qa/report/bench4.jpg").tagoe().nl();
		x.tago("img").attr("src","/qa/report/bench5.jpg").tagoe().nl();
		x.nl().nl();
		b.path("/qa/report/sysinfo.out").to(x);
	}
}