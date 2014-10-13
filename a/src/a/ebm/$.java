package a.ebm;
import java.nio.ByteBuffer;

import b.*;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.pl("virtually old computer");
	}

	static class io{
		int nports_in=2;
		int nports_out=2;
		final port_in[]in=new port_in[nports_in];
		final port_out[]out=new port_out[nports_out];
	}
	static class port_in{void receive(final ByteBuffer bb,final on_done x){}}
	static class port_out{void send(final ByteBuffer bb,final on_done x){}}
	interface on_done{void x(final int code);}
	static class computer{
		final io io=new io();
		final core cpu=new core();
		final ram ram=new ram();
	}
	static class core{}
	static class ram{}
	static class storage_tape{
		final io io=new io();
		void record_seek(final int position){}
		void record_read(){}
		void record_write(){}
		byte[]record_get(){return null;}
		void record_set(byte[]ba){}
	}
	static class output_printer{
		final io io=new io();
		void print(final ByteBuffer bb){}
		void new_line(){}
//		void carriage_return(){}
		void bell(){}
		
	}
//	static class network{}

	static final long serialVersionUID=1;
}