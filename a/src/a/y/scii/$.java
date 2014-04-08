package a.y.scii;

import a.y.scii.medusa.sprite;
import a.y.scii.medusa.sprite_image;
import b.a;
import b.xwriter;
public class $ extends a{
	@Override public void to(final xwriter x)throws Throwable{
//		x.pl("medusa ascii game").nl();
		x.ax(this,"reset").nl().nl();
		final sprite_image spi=new sprite_image();
		spi.load();
		final sprite sp=new sprite();
		sp.sprite_image=spi;
		sp.x=3;
		sp.y=2;
		mds.sprites.add(sp);
		
		mds.update();
		mds.scr.clear('.');
		mds.draw(mds.scr);
		mds.scr.screen_to_outputstream(x.outputstream());
	}
	public void x_reset(xwriter x,String a){
		mds.rst();
		x.xreload();
	}
	private medusa mds=new medusa();
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
}
