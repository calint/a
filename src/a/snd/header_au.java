package a.snd;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

final public class header_au implements Serializable,header{
	final public byte[]ba=new byte[]{// from  http://en.wikipedia.org/wiki/Au_file_format.
/*0*/	'.','s','n','d',//magic number
/*4*/	0,0,0,0x20,//data offset
/*8*/	-1,-1,-1,-1,//data size  default unknown 0xff
/*12*/	0,0,0,1,//encoding
/*16*/	0,0,0x1f,0x40,//samples/second
/*20*/	0,0,0,1,//channels
/*24*/	0,0,0,0,
/*28*/	0,0,0,0,
	};
	@Override public void set_data_size_in_bytes(final int n){set_int_in_bytes_array(n,8);}
	@Override public void set_samples_per_second(final int n){set_int_in_bytes_array(n,16);}
	@Override public void set_number_of_channels(final int n){set_int_in_bytes_array(n,20);}
	@Override public void to(OutputStream os)throws IOException{os.write(ba);}

	private void set_int_in_bytes_array(final int n,final int offset_starting_at_zero){
		ba[offset_starting_at_zero  ]=(byte)((n&0xff000000)>>24);
		ba[offset_starting_at_zero+1]=(byte)((n&0x00ff0000)>>16);
		ba[offset_starting_at_zero+2]=(byte)((n&0x0000ff00)>> 8);
		ba[offset_starting_at_zero+3]=(byte)((n&0x000000ff)    );
	}

	private static final long serialVersionUID=1;
}
