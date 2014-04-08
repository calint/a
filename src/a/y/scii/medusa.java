package a.y.scii;

import java.io.Serializable;
import java.util.ArrayList;

public class medusa implements Serializable{
	public screen scr;
	public int frame;
	public medusa(){
		scr=new screen(80,40);
	}
	public void rst(){
		scr.rst();
		frame=0;
		sprites.clear();
		
		final sprite_image spi=new sprite_image();
		spi.load();
		
		final sprite sp=new sprite();
		sp.sprite_image=spi;
		sp.x=3;
		sp.y=2;
		sprites.add(sp);
		
		final sprite sp2=new sprite();
		sp2.sprite_image=spi;
		sp2.x=30;
		sp2.y=8;
		sprites.add(sp2);
	}
	public void update(){sprites.forEach(sprite::update);}

	public void draw(){draw(scr);}
	public void draw(final screen s){
		scr.clear('.');
		sprites.forEach((sprite sp)->sp.draw(s));
	}
	
	public final ArrayList<sprite>sprites=new ArrayList<>(128);
	private static final long serialVersionUID=1L;
}