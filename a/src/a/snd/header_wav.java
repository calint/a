package a.snd;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

final public class header_wav implements Serializable,header{
	final public byte[]ba=new byte[]{
/*0*/	'R','I','F','F',//ChunkID   Contains the letters "RIFF" in ASCII form
/*4*/	36,0,0,0,//ChunkSize    36 + SubChunk2Size
/*8*/	'W','A','V','E',//Format  Contains the letters "WAVE"
/*12*/	'f','m','t',' ',//Subchunk1ID    Contains the letters "fmt "
/*16*/	16,0,0,0,//Subchunk1Size    16 for PCM.
/*20*/	1,0,//AudioFormat    PCM = 1
/*22*/	1,0,//NumChannels   Mono = 1, Stereo = 2, etc.
/*24*/	0x40,0x1f,0,0,//SampleRate    8000, 44100, etc.
/*28*/	0,0,0,0,//ByteRate  == SampleRate * NumChannels * BitsPerSample/8
/*32*/	0,0,//BlockAlign  == NumChannels * BitsPerSample/8
/*34*/	8,0,//BitsPerSample   8 bits = 8, 16 bits = 16, etc.
/*36*/	'D','A','T','A',//Subchunk2ID
/*40*/	0,0,0,0,//Subchunk2Size == NumSamples * NumChannels * BitsPerSample/8
	};
	@Override public void set_data_size_in_bytes(final int n){set_int_in_bytes_array(n,40);recalc();}
	@Override public void set_samples_per_second(final int n){set_int_in_bytes_array(n,24);recalc();}
	@Override public void set_number_of_channels(final int n){set_short_in_bytes_array((short)n,22);recalc();}
	@Override public void to(OutputStream os)throws IOException{os.write(ba);}
	private void recalc(){
//		final short bps=get_short_from_bytes_array(34);
//		set_short_in_bytes_array(bps,offset_starting_at_zero)
	}
	
	private void set_int_in_bytes_array(final int n,final int offset_starting_at_zero){
		ba[offset_starting_at_zero  ]=(byte)((n&0x000000ff)    );
		ba[offset_starting_at_zero+1]=(byte)((n&0x0000ff00)>> 8);
		ba[offset_starting_at_zero+2]=(byte)((n&0x00ff0000)>>16);
		ba[offset_starting_at_zero+3]=(byte)((n&0xff000000)>>24);
	}
	private void set_short_in_bytes_array(final short n,final int offset_starting_at_zero){
		ba[offset_starting_at_zero  ]=(byte)((n&0x000000ff)    );
		ba[offset_starting_at_zero+1]=(byte)((n&0x0000ff00)>> 8);
	}

	private static final long serialVersionUID=1;
}
