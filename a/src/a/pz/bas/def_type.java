package a.pz.bas;

import java.io.IOException;
import b.xwriter;

final public class def_type extends def{
	public def_type(final program r) throws IOException{
		super(r);
		name=r.next_identifier();
		txt=new xwriter().p("typedef").spc().p(name).toString();
	}
	private static final long serialVersionUID=1;
}