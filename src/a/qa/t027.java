package a.qa;
import b.*;
// osjsstr encoders
final public class t027 extends a{static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.script().xalert("abc \'\" ``\nhello\r\\\0").scriptEnd();
	}
}