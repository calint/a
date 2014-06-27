package a.qa;
import b.*;
// osltgt,osnl encoders
final public class t026 extends a{static final long serialVersionUID=1;
	@SuppressWarnings("resource")
	public void to(final xwriter x)throws Throwable{
		b.path("qa/t000.html").to(new osltgt(new osnl(){
	        public void onnewline(final String line) throws Throwable{
		        x.pl(line);
	        }
        }));
		x.nl().nl();
		new osltgt(x.outputstream()).write("<tag>".getBytes());
		x.nl().nl();
		try{new osltgt(x.outputstream()).write(' ');}catch(final Throwable t){x.pl("osltgt.write.ch");}
		try{new osnl().write(' ');}catch(final Throwable t){x.pl("osnl.write.ch");}
    }
}