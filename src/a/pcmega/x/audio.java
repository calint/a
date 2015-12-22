package a.pcmega.x;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import a.pczero.vintage;
import b.a;
import b.xwriter;
final public class audio extends a{
	static final long serialVersionUID=1;
//	final int samplerate=44100;
//	final int samplerate=22100;
//	final int samplerate=16000;
	final int samplerate=8000;
//	final int channels=2;
	final int channels=1;
	final AudioFormat af=new AudioFormat((float)samplerate,16,channels,true,true);
	final SourceDataLine sdl;
	final int bufferSize=samplerate>>1;
	public audio()throws LineUnavailableException{
		sdl=AudioSystem.getSourceDataLine(af);
		sdl.open(af,bufferSize);
		sdl.start();
	}
	private final byte[]ba=new byte[2];
//	final byte[]ba=new byte[4];
	public int avail(){return sdl.available();}
	public void write(final short d0){
//		final short d0=(short)(Math.sin(a)*0x7f);
//		final short d1=(short)(Math.random()*0x7f);
		if(af.isBigEndian()){
			ba[0]=(byte)(d0&0xff);
			ba[1]=(byte)(d0>>8);
//			ba[3]=(byte)(d1>>8);
//			ba[2]=(byte)d1;
		}else{
			ba[0]=(byte)(d0>>8);
			ba[1]=(byte)d0;
//			ba[2]=(byte)(d1>>8);
//			ba[3]=(byte)d1;
		}
//		x.pl(""+sdl.available());
		sdl.write(ba,0,ba.length);
	}
	public void to(final xwriter x)throws Throwable{
		double a=0,da=Math.PI/0x10;
		long sample=0;
//		short d=0;
		x.pre();
		x.p(". ").p(Integer.toHexString(samplerate)).nl();
		x.p(".");
		try{while(true){
			final short d=(short)(Math.sin(a)*0x7f);
			write(d);
			a+=da;
			x.spc().p(vintage.fld("0000",Integer.toHexString(d)));
			if((++sample)%samplerate==0){
				x.nl().p(".").flush();
			}
		}}finally{
//			sdl.drain();
//			sdl.stop();
//			sdl.close();
		}
	}
}
