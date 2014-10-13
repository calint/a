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
	static class computer extends component{
		final core cpu=new core();
		final ram ram=new ram();
	}
	static class core{}
	static class ram{}
	static class storage_tape extends component{
		void record_seek(final int position){}
		byte[]record_read(){return new byte[0];}
		void record_write(final ByteBuffer bb){}
		void record_write(final ByteBuffer bb,final int offset_in_bytes){}
	}
	static class output_printer extends component{
		void print(final ByteBuffer bb){}
		void new_line(){}
//		void carriage_return(){}
		void bell(){}
		
	}
	static class component{
		final io io=new io();		
	}
//	static class network{}

	static final long serialVersionUID=1;
}