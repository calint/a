package a.y.scii;

import b.a;
import b.xwriter;
public class $ extends a{
	@Override public void to(final xwriter x)throws Throwable{
		x.pl("medusa ascii game").nl();
		mds.print("hello world");
		mds.screen_to_outputstream(x.outputstream());
	}
	
	private medusa mds=new medusa();
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
}
