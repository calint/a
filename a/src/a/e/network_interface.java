package a.e;

import b.a;
import b.xwriter;

public class network_interface extends a{
	@Override public void to(xwriter x)throws Throwable{
		x.pl("network interface");
	}
	
	
	
	public void broadcast(final packet np)throws Throwable{}
	public void send(final packet np)throws Throwable{}

	private static final long serialVersionUID=1;
}
