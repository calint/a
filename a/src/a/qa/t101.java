package a.qa;
import b.*;
public class t101 extends a implements bin{static final long serialVersionUID=1;
	public String contenttype(){return "text/plain;charset=utf8";}
	public void to(final xwriter x)throws Throwable{
		x.pl("coverage of thdwatch").nl();
		b.thd_watch=false;
		thdwatch.reset();
		thdwatch.print_fields2_to(new osnl(){public void onnewline(final String line) throws Throwable{
				x.pl(line);
		}},new byte[]{'\n'},new byte[]{'\n','\n'},"          ");
		b.thd_watch=true;
	}
}
