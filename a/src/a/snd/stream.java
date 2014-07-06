package a.snd;
import static b.b.pl;
import java.io.OutputStream;
import b.a;
import b.bin;
import b.cacheable;
import b.req;
import b.xwriter;
final public class stream extends a implements cacheable,bin{
	@Override public String filetype(){return"au";}
	@Override public String contenttype(){return"audio/basic";}
	@Override public boolean cacheforeachuser(){return false;}
	@Override public long lastmodupdms(){return 1000;}
	@Override public String lastmod(){return null;}

	@Override public void to(final xwriter x)throws Throwable{
		final String range=req.get().headers().get("range");//k.ocʏ
		pl(" •. stream "+range);
		pl(" •· "+req.get().headers());
		final int range_from_byte,range_to_byte;
//		if(range!=null)
//			throw new Error("range not supported");
//		if(range!=null){
//			final String from_to_in_bytes=range.split("=")[1];
//			final String[]ft=from_to_in_bytes.split("-");
//			range_from_byte=Integer.parseInt(tostr(ft[0],"0"));
//			range_to_byte=Integer.parseInt(tostr(ft[1],"0"));
//		}else{
			range_from_byte=0;
			range_to_byte=0x1000;
//		}
		final header_au h=new header_au();
		final int size_in_bytes=range_to_byte-range_from_byte;
		h.set_data_size_in_bytes(size_in_bytes);
//		h.set_samples_per_second(8000);
		final OutputStream os=x.outputstream();
		h.to(os);
		final byte[]ba=new byte[size_in_bytes];
		double da_dt=Math.PI*2/8000*5000;
		double a=0;
		for(int i=0;i<size_in_bytes;i++){
			ba[i]=(byte)(Math.sin(a+=da_dt)*127);
			da_dt+=Math.PI*2/8000;
		}
		os.write(ba);
	}
	
	
	static final long serialVersionUID=1;
}