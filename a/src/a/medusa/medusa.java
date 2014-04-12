package a.medusa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class medusa implements Serializable{
	public static interface medusa_ctor{
		void init(final medusa m)throws Throwable;
	}
	public static String ctor_class_name="a.medusa.level.ctor";
//	private int frame;
	public void rst()throws Throwable{
		players_all.clear();
		final medusa_ctor ctor=(medusa_ctor)Class.forName(ctor_class_name).newInstance();
		ctor.init(this);
//		frame=0;
		
//		final sprite spi=new sprite();
//		spi.load();
//		
//		player sp=new player();
//		sp.glo(spi).xy(29,2);
//		players_all.add(sp);
//		
//		sp=new player();
//		sp.glo(spi).xy(30,8);
//		players_all.add(sp);
//		
//		sp=new player();
//		sp.glo(spi).xy(70,30);
//		players_all.add(sp);
//
//		sp=new player();
//		sp.glo(spi).xy(53,34);
//		players_all.add(sp);
//
//		sp=new player();
//		final glo_circle_xy circ=new glo_circle_xy();
//		circ.load();
//		sp.glo(circ).xy(20,20).da(10*dtor);
//		players_all.add(sp);

		players_free.clear();
		players_free.addAll(players_all);		
	}
	public final static float dtor=(float)(Math.PI/180);
	public static float dtor(int degrees){return degrees*dtor;}
	public void update(final float dt){
		last_update_dt_s=dt;
		players_all.forEach((glob g)->g.update(dt));
	}
	public void draw(final screen s){
		s.clear('.');
		players_all.forEach((glob g)->g.draw(s));
//		players_all.forEach(glob::draw);
	}
	public player alloc_sprite_for_new_player(){if(players_free.isEmpty())return null;return players_free.removeFirst();}
	public void on_player_closed_connection(final player s){players_free.add(s);}
	public final ArrayList<player>players_all=new ArrayList<>(128);
	public final LinkedList<player>players_free=new LinkedList<>();
	public float last_update_dt_s;
	
	public static @interface readonly{}
	public static @interface takes{}
	public static @interface gives{}
	private static final long serialVersionUID=1L;
}