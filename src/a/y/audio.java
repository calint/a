package a.y;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import b.a;
import b.xwriter;


public class audio extends a {
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.pre();
		for(final AudioFileFormat.Type aft:AudioSystem.getAudioFileTypes()){
			x.pl(aft.toString());
		}
	}
}
