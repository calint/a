package a.medusa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class medusa implements Serializable{
	public static String plugin="a.medusa.level.ctor";
	public void rst()throws Throwable{
		players_all.clear();
		Class.forName(plugin).asSubclass(medusa_plugin.class).newInstance().init(this);
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
	private final ArrayList<player>players_all=new ArrayList<>(128);
	final public medusa players_add(final player sp){players_all.add(sp);return this;}
	final public player players_get(final int player){return players_all.get(player);}
	public final LinkedList<player>players_free=new LinkedList<>();
	final public boolean has_active_players(){return players_free.size()==players_all.size();}
	private float last_update_dt_s;
	final public float dt(final float s){return s*last_update_dt_s;}
	
	public static @interface readonly{}
	public static @interface takes{}
	public static @interface gives{}
	public static interface medusa_plugin{void init(final medusa m)throws Throwable;}
	private static final long serialVersionUID=1L;
}