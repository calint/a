package a;
import static b.b.tostr;
import b.a;
import b.req;
import b.xwriter;
public class utf extends a{
	private static final long serialVersionUID=1;
	public void to(xwriter x) throws Throwable{
		final int line_width=33;
		final int chars=Integer.parseInt(tostr(req.get().query(),Integer.toString(0x20000)));
		final int lines=chars/line_width;
		x.style().p("*{font-family:monospaced;font-size:32px}").styleEnd();
		x.tag("center").code().pre();
		char ch=0;
//		final int lines=0x20000/33;
		for(int n=0;n<=lines;n++){
			for(int c=0;c<line_width;c++){
				if(ch>chars)break;//? outerloopcheck
				x.p(ch);
				ch++;
			}
			x.nl();
		}
		x.codeEnd();
	}
}
//ڀ