package a.y.mixer;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import b.a;
import b.xwriter;
public class $ extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
	if(pt()==null)x.style("html","margin:0 8em 0 8em;padding:0 3em 0 3em;box-shadow:0 0 .5em rgba(0,0,0,.5);border-radius:0em");
	x.ax(this).p("play").nl();
	x.style(s,"width:100%;height:200px;border:1px dotted green").inputTextArea(s);
	x.nl();
	x.pl("mixer");
	x.pl("   data source");
	x.pl("      sound file");
	x.pl("      internet");
	x.pl("   data source span");
	x.pl("      sound");
	x.pl("      video");
	x.pl("   program");
	x.pl("      script");
	x.pl("      tracks");
	x.pl("   output");
	x.pl("      au");
	x.style(o,"display:block;width:100%;height:200px;border:1px dotted green").output(o);
}
	synchronized public void x_(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		
		float frameRate = 44100f; // 44100 samples/s
		int channels = 2;
		double duration = 1.0;
		int sampleBytes = Short.SIZE / 8;
		int frameBytes = sampleBytes * channels;
		AudioFormat format =
		    new AudioFormat(Encoding.PCM_SIGNED,
		                    frameRate,
		                    Short.SIZE,
		                    channels,
		                    frameBytes,
		                    frameRate,
		                    true);
		int nFrames = (int) Math.ceil(frameRate * duration);
		int nSamples = nFrames * channels;
		int nBytes = nSamples * sampleBytes;
		ByteBuffer data = ByteBuffer.allocate(nBytes);
		double freq = 440.0;
		// Generate all samples
		for ( int i=0; i<nFrames; ++i )
		{
		  double value = Math.sin((double)i/(double)frameRate*freq*2*Math.PI)*(Short.MAX_VALUE);
		  for (int c=0; c<channels; ++ c) {
		      int index = (i*channels+c)*sampleBytes;
		      data.putShort(index, (short) value);
		  }
		}

		AudioInputStream stream =
		    new AudioInputStream(new ByteArrayInputStream(data.array()), format, nFrames*2);
		Clip clip = AudioSystem.getClip();
		clip.open(stream);
		clip.start();
		clip.drain();
	}
	
	public a s;{s.from(getClass().getResourceAsStream("default.mix"),"");}
	public a o;
}