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
		
		final image spi=new image();
		spi.load();
		
		sprite sp=new sprite().image(spi).xy(3,2);
		sprites.add(sp);
		
		sp=new sprite().image(spi).xy(30,8);
		sprites.add(sp);
		
		sp=new sprite().image(spi).xy(60,8);
		sprites.add(sp);

		sp=new sprite().image(spi).xy(53,34);
		sprites.add(sp);

		sprites_available_for_new_players.clear();
		sprites_available_for_new_players.addAll(sprites);		
	}
	public void update(){sprites.forEach(sprite::update);}

	public void draw(){draw(scr);}
	final static float dt=1/10.f;
	private float[]dot=new float[]{0,0};
	private float[]ddot=new float[]{.5f,.5f};
	static void add2(final float[]dest_xy,final float[]xy){
		dest_xy[0]+=xy[0];dest_xy[1]+=xy[1];//? simd
	}
	static void add2(final float[]dest_xy,final float[]xy,final float scale){
		dest_xy[0]+=scale*xy[0];dest_xy[1]+=scale*xy[1];//? simd
	}
	public void draw(final screen s){
		scr.clear('.');
		/// background layers
		scr.render_dot(dot,0);
		add2(dot,ddot);
		scr.render_rect(new float[]{15,25},new float[]{4,3});
		scr.render_rect(new float[]{21,24},new float[]{10,4});
		scr.render_rect(new float[]{32,21},new float[]{10,7});
		//
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