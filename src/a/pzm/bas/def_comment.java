package a.pzm.bas;

import java.io.IOException;

final public class def_comment extends def{
	protected String line;
	public def_comment(program p) throws IOException{
		super(p);
		line=p.consume_rest_of_line();
		txt="//"+line;
	}
	private static final long serialVersionUID=1;
}