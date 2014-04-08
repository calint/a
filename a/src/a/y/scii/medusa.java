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
	}
	public void update(){sprites.forEach(sprite::update);}
	public void draw(final screen s){sprites.forEach((sprite sp)->sp.draw(s));}
	
	public final ArrayList<sprite>sprites=new ArrayList<>(128);
	private static final long serialVersionUID=1L;
}