package a.ebm;
import java.nio.ByteBuffer;

import b.*;
final public class $ extends a{
	public void to(final xwriter x)throws Throwable{
		x.pl("virtually old computer");
	}

	static class io{
		final int nports_in;
		final int nports_out;
		final port_in[]in;
		final port_out[]out;
		io(final int n_ports_in,final int n_ports_out){
			nports_in=n_ports_in;
			nports_out=n_ports_out;
			in=new port_in[nports_in];
			out=new port_out[nports_out];
		}
	}
	static class port_in{void receive(final ByteBuffer bb,final on_done x){}}
	static class port_out{void send(final ByteBuffer bb,final on_done x){}}
	interface on_done{void x(final int code);}
	static class cpu extends component{
		private final core core=new core(this);
		private final ram ram;
		cpu(final ram r){super(new io(1,1));ram=r;}
	}
	static class core{
		private final cpu cpu;
		core(final cpu c){cpu=c;}
	}
	static class ram{
		private final int[]data;
		ram(final int locations_in_ints){data=new int[locations_in_ints];}
		final int read(final int location){return data[location];}
		final void write(final int location,final int dat){data[location]=dat;}
	}
	static class computer extends component{
		private final ram ram=new ram(64*1024);
		private final cpu cpu=new cpu(ram);
		computer(){super(new io(1,1));}
	}
	static class storage_tape extends component{
		storage_tape(){super(new io(1,1));}
		void record_seek(final int position){}
		byte[]record_read(){return new byte[0];}
		void record_write(final ByteBuffer bb){}
		void record_write(final ByteBuffer bb,final int offset_in_bytes){}
	}
	static class output_printer extends component{
		output_printer(){super(new io(1,0));}
		void print(final ByteBuffer bb){}
		void new_line(){}
//		void carriage_return(){}
		void bell(){}
		
	}
	static class component{
		component(final io io){this.io=io;}
		private final io io;		
	}
//	static class network{}

	static final long serialVersionUID=1;
}