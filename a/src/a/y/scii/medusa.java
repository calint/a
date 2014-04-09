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
		
		sprite sp=new sprite().image(spi).xy(0,2);
		sprites.add(sp);
		
		sp=new sprite().image(spi).xy(30,8);
		sprites.add(sp);
		
		sp=new sprite().image(spi).xy(70,30);
		sprites.add(sp);

		sp=new sprite().image(spi).xy(53,34);
		sprites.add(sp);

		sprites_available_for_new_players.clear();
		sprites_available_for_new_players.addAll(sprites);		
	}
	public void update(){sprites.forEach(sprite::update);}

	public void draw(){draw(scr);}
	final static float dt=1/10.f;
	static void add2(final float[]dest_xy,final float[]xy){
		dest_xy[0]+=xy[0];dest_xy[1]+=xy[1];//? simd
	}
	static void add2(final float[]dest_xy,final float[]xy,final float scale){
		dest_xy[0]+=scale*xy[0];dest_xy[1]+=scale*xy[1];//? simd
	}
	static final void vertices_rotate_about_z_axis(final float[]dst,final/*readonly*/float[]src,final float angle_in_radians,final/*read*/float[]translation){
		// |cos -sin||x|=|xcos-ysin|
		// |sin  cos||y|=|xsin+ycos|
		System.out.println(angle_in_radians*180/Math.PI);
		final float c=(float)Math.cos(angle_in_radians);
		final float s=(float)Math.sin(angle_in_radians);
		final float tx=translation[0];
		final float ty=translation[1];		
		for(int i=0;i<src.length;i+=2){
			final float x=src[i];
			final float y=src[i+1];
			final float x1=x*c-y*s+tx;
			final float y1=x*s+y*c+ty;
			dst[i]=x1*2;
			dst[i+1]=y1;
		}
	}
	float a,da=(float)Math.PI/180*10;
	private float[]dot=new float[]{40,20};
	private float[]ddot=new float[]{.5f,.5f};
	public void draw(final screen s){
		scr.clear('.');
		/// background layers
//		add2(dot,ddot);
//		scr.render_rect(new float[]{15,25},new float[]{4,3});
//		scr.render_rect(new float[]{21,24},new float[]{10,4});
//		scr.render_rect(new float[]{32,21},new float[]{10,7});
		
		
//		scr.render_convex_polygon(new float[]{40,20, 20,20, 40,30},3);
//		scr.render_convex_polygon(new float[]{40,5, 20,5, 40,15},3);
//		final float[]vertices_xy=new float[]{0,5, -10,-5, 20,-5};
		
		final float[]vertices_xy=new float[]{0,-10, -10,5, 10,5};
		vertices_rotate_about_z_axis(vertices_xy,vertices_xy,2*(float)Math.PI/180*sprites.get(0).phys.pos[0],new float[]{20,20});
		a+=da*dt;
		scr.render_convex_polygon(vertices_xy,3);

		scr.render_dot(dot,(byte)'X');
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