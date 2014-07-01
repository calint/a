package a.e;

import b.a;
import b.xwriter;

public class unit extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.pl(" • unit");
		 x.p("  • ");ni.to(x);
	}
	
	
	public network_interface ni;
	public unit(){}
	public void recv(final packet p){}
	public void send(final packet p){}
	
	private static final long serialVersionUID=1;
}
