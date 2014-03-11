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
//	x.ax(this).p("play").nl();
	x.ax(this,"base").spc();
	x.ax(this,"blip").spc();
	x.ax(this,"blop").spc();
	x.ax(this,"blah").spc();
	x.ax(this,"creak").spc();
//	x.style(s,"width:100%;height:200px;border:1px dotted green").inputTextArea(s);
//	x.nl();
//	x.pl("mixer");
//	x.pl("   data source");
//	x.pl("      sound file");
//	x.pl("      internet");
//	x.pl("   data source span");
//	x.pl("      sound");
//	x.pl("      video");
//	x.pl("   program");
//	x.pl("      script");
//	x.pl("      tracks");
//	x.pl("   output");
//	x.pl("      au");
//	x.style(o,"display:block;width:100%;height:200px;border:1px dotted green").output(o);
}
	synchronized public void x_(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		
//		final float frameRate=44100f;// 44100 samples/s
		final float frameRate=500f;// phone quality
		final int channels=2;
		final double duration=1;
		final int sampleBytes=Short.SIZE/8;
		final int frameBytes=sampleBytes*channels;
		final AudioFormat format=new AudioFormat(Encoding.PCM_SIGNED,frameRate,Short.SIZE,channels,frameBytes,frameRate,true);
		final int nFrames=(int)Math.ceil(frameRate*duration);
		final int nSamples=nFrames*channels;
		final int nBytes=nSamples*sampleBytes;
		final ByteBuffer data=ByteBuffer.allocate(nBytes);
		double freq=440.0;
		for(int i=0;i<nFrames;++i){
			final double value=Math.sin((double)i/(double)frameRate*freq*2*Math.PI)*(Short.MAX_VALUE);
			freq+=0.1;
			for(int c=0;c<channels;++c){
				final int index=(i*channels+c)*sampleBytes;
				data.putShort(index,(short)value);
			}
		}
		final AudioInputStream stream=new AudioInputStream(new ByteArrayInputStream(data.array()),format,nFrames*2);
		final Clip clip=AudioSystem.getClip();
		clip.open(stream);
		clip.start();
		clip.drain();
		clip.close();
	}
	public void x_base(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		
//		final float frameRate=44100f;// 44100 samples/s
		final float frameRate=500f;// phone quality
		final int channels=2;
		final double duration=1;
		final int sampleBytes=Short.SIZE/8;
		final int frameBytes=sampleBytes*channels;
		final AudioFormat format=new AudioFormat(Encoding.PCM_SIGNED,frameRate,Short.SIZE,channels,frameBytes,frameRate,true);
		final int nFrames=(int)Math.ceil(frameRate*duration);
		final int nSamples=nFrames*channels;
		final int nBytes=nSamples*sampleBytes;
		final ByteBuffer data=ByteBuffer.allocate(nBytes);
		double freq=440.0;
		for(int i=0;i<nFrames;++i){
			final double value=Math.sin((double)i/(double)frameRate*freq*2*Math.PI)*(Short.MAX_VALUE);
			freq+=0.1;
			for(int c=0;c<channels;++c){
				final int index=(i*channels+c)*sampleBytes;
				data.putShort(index,(short)value);
			}
		}
		final AudioInputStream stream=new AudioInputStream(new ByteArrayInputStream(data.array()),format,nFrames*2);
		final Clip clip=AudioSystem.getClip();
		clip.open(stream);
		clip.start();
		clip.drain();
		clip.close();
	}
	public void x_blip(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		
//		final float frameRate=44100f;// 44100 samples/s
		final float frameRate=200f;// phone quality
		final int channels=2;
		final double duration=1;
		final int sampleBytes=Short.SIZE/8;
		final int frameBytes=sampleBytes*channels;
		final AudioFormat format=new AudioFormat(Encoding.PCM_SIGNED,frameRate,Short.SIZE,channels,frameBytes,frameRate,true);
		final int nFrames=(int)Math.ceil(frameRate*duration);
		final int nSamples=nFrames*channels;
		final int nBytes=nSamples*sampleBytes;
		final ByteBuffer data=ByteBuffer.allocate(nBytes);
		double freq=440.0;
		for(int i=0;i<nFrames;++i){
			final double value=Math.sin((double)i/(double)frameRate*freq*2*Math.PI)*(Short.MAX_VALUE);
			freq+=0.1;
			for(int c=0;c<channels;++c){
				final int index=(i*channels+c)*sampleBytes;
				data.putShort(index,(short)value);
			}
		}
		final AudioInputStream stream=new AudioInputStream(new ByteArrayInputStream(data.array()),format,nFrames*2);
		final Clip clip=AudioSystem.getClip();
		clip.open(stream);
		clip.start();
		clip.drain();
		clip.close();
	}
	public void x_blop(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		
//		final float frameRate=44100f;// 44100 samples/s
		final float frameRate=100f;// phone quality
		final int channels=2;
		final double duration=1;
		final int sampleBytes=Short.SIZE/8;
		final int frameBytes=sampleBytes*channels;
		final AudioFormat format=new AudioFormat(Encoding.PCM_SIGNED,frameRate,Short.SIZE,channels,frameBytes,frameRate,true);
		final int nFrames=(int)Math.ceil(frameRate*duration);
		final int nSamples=nFrames*channels;
		final int nBytes=nSamples*sampleBytes;
		final ByteBuffer data=ByteBuffer.allocate(nBytes);
		double freq=440.0;
		for(int i=0;i<nFrames;++i){
			final double value=Math.sin((double)i/(double)frameRate*freq*2*Math.PI)*(Short.MAX_VALUE);
			freq+=0.1;
			for(int c=0;c<channels;++c){
				final int index=(i*channels+c)*sampleBytes;
				data.putShort(index,(short)value);
			}
		}
		final AudioInputStream stream=new AudioInputStream(new ByteArrayInputStream(data.array()),format,nFrames*2);
		final Clip clip=AudioSystem.getClip();
		clip.open(stream);
		clip.start();
		clip.drain();
		clip.close();
	}
	synchronized public void x_blah(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		new Thread(new Runnable(){public void run(){try{
			$.this.x_base(x,q);
		}catch(final Throwable t){throw new Error(t);}}}).start();
		new Thread(new Runnable(){public void run(){try{
			$.this.x_blip(x,q);
		}catch(final Throwable t){throw new Error(t);}}}).start();
		new Thread(new Runnable(){public void run(){try{
			$.this.x_blop(x,q);
		}catch(final Throwable t){throw new Error(t);}}}).start();
	}
	public void x_creak(final xwriter x,final String q)throws Throwable{
		x.xu(o,""+System.currentTimeMillis());
		
//		final float frameRate=44100f;// 44100 samples/s
//		final float frameRate=100f;// phone quality
		final float frameRate=500f;// phone quality
		final int channels=2;
		final double duration=.3;
		final int sampleBytes=Short.SIZE/8;
		final int frameBytes=sampleBytes*channels;
		final AudioFormat format=new AudioFormat(Encoding.PCM_SIGNED,frameRate,Short.SIZE,channels,frameBytes,frameRate,true);
		final int nFrames=(int)Math.ceil(frameRate*duration);
		final int nSamples=nFrames*channels;
		final int nBytes=nSamples*sampleBytes;
		final ByteBuffer data=ByteBuffer.allocate(nBytes);
		double freq=440.0;
		for(int i=0;i<nFrames;++i){
			final double value=Math.sin((double)i/(double)frameRate*freq*2*Math.PI)*(Short.MAX_VALUE);
			freq+=1;
			for(int c=0;c<channels;++c){
				final int index=(i*channels+c)*sampleBytes;
				data.putShort(index,(short)value);
			}
		}
		final AudioInputStream stream=new AudioInputStream(new ByteArrayInputStream(data.array()),format,nFrames*2);
		final Clip clip=AudioSystem.getClip();
		clip.open(stream);
		clip.start();
		clip.drain();
		clip.close();
	}
	public a s;{s.from(getClass().getResourceAsStream("default.mix"),"");}
	public a o;
}