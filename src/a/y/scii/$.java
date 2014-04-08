package a.y.scii;

import a.y.scii.medusa.sprite;
import a.y.scii.medusa.sprite_image;
import b.a;
import b.xwriter;
public class $ extends a{
	public a div;
	@Override public void to(final xwriter x)throws Throwable{
//		x.pl("medusa ascii game").nl();
		x.ax(this,"reset",":: reset").spc().p("use wasd keys to move medusa").nl().nl();
		
//		x.style(".scr","border:1px solid black;");
//		x.div("scr");
//		x.p("<div onkeydown='alert(event);return false;'>");
		x.script().nl();
		x.pl("ui.keys['W']=\"$x('_ keyb w')\"");
		x.pl("ui.keys['A']=\"$x('_ keyb a')\"");
		x.pl("ui.keys['S']=\"$x('_ keyb s')\"");
		x.pl("ui.keys['D']=\"$x('_ keyb d')\"");
		x.pl("document.focus();");
		x.scriptEnd();

		step();
		x.el(div);
		mds.scr.screen_to_outputstream(x.outputstream());
		x.elend();
	}
	public void step(){
		mds.update();
		mds.scr.clear('.');
		mds.draw(mds.scr);		
	}
	public void x_reset(xwriter x,String a){
		mds.rst();
		final sprite_image spi=new sprite_image();
		spi.load();
		final sprite sp=new sprite();
		sp.sprite_image=spi;
		sp.x=3;
		sp.y=2;
		mds.sprites.add(sp);
		x.xreload();
	}
	public void x_keyb(xwriter x,String key){
		if("a".equals(key)){
			mds.sprites.get(0).x--;
		}else if("d".equals(key)){
			mds.sprites.get(0).x++;
		}else if("w".equals(key)){
			mds.sprites.get(0).y--;
		}else if("s".equals(key)){
			mds.sprites.get(0).y++;
		}
		step();
		final xwriter xx=x.xub(div,true,true);
		mds.scr.screen_to_outputstream(xx.outputstream());
		x.xube();
//		x.xreload();
	}
	private medusa mds=new medusa();
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
}
