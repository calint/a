package a;
import b.a;
import b.xwriter;
public class utf extends a{
	private static final long serialVersionUID=1;
	public void to(xwriter x) throws Throwable{
		x.style().p("*{font-family:monospaced;font-size:32px}").styleEnd();
		x.tag("center").code().pre();
		char ch=0;
		final int lines=0x20000/33;
		for(int n=0;n<lines;n++){
			for(int c=0;c<33;c++){
				x.p(ch);
				ch++;
			}
			x.nl();
		}
		x.codeEnd();
	}
}
//ڀ