package e;

import b.a;

public class network_interface extends a{
	public static interface recv{void network_interface_recv(final packet p);}
	recv rv;
	public network_interface receiver(final recv r){rv=r;return this;}
	public void broadcast(final packet np)throws Throwable{}
	public void send(final packet np)throws Throwable{}
	private static final long serialVersionUID=1;
}
