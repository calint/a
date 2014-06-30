package e;

import b.a;

public class unit extends a implements network_interface.recv{
	final public network_interface ni;
	public unit(final network_interface n){ni=n;}
	@Override public void network_interface_recv(final packet p){}
	private static final long serialVersionUID=1;
}
