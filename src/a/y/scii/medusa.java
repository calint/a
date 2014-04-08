package a.y.scii;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

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
		
		sprite sp=new sprite();
		sp.sprite_image=spi;
		sp.x=3;
		sp.y=2;
		sprites.add(sp);
		
		sp=new sprite();
		sp.sprite_image=spi;
		sp.x=30;
		sp.y=8;
		sprites.add(sp);
		
		sp=new sprite();
		sp.sprite_image=spi;
		sp.x=60;
		sp.y=8;
		sprites.add(sp);

		sp=new sprite();
		sp.sprite_image=spi;
		sp.x=53;
		sp.y=34;
		sprites.add(sp);

		sprites_available_for_new_players.clear();
		sprites_available_for_new_players.addAll(sprites);
	}
	public void update(){sprites.forEach(sprite::update);}

	public void draw(){draw(scr);}
	public void draw(final screen s){
		scr.clear('.');
		sprites.forEach((sprite sp)->sp.draw(s));
	}
	public sprite alloc_sprite_for_new_player(){
		if(sprites_available_for_new_players.isEmpty())return null;
		return sprites_available_for_new_players.removeFirst();
	}
	public void on_player_closed_connection(sprite s){
		sprites_available_for_new_players.add(s);
	}
	public final ArrayList<sprite>sprites=new ArrayList<>(128);
	public final LinkedList<sprite>sprites_available_for_new_players=new LinkedList<>();
	private static final long serialVersionUID=1L;
}