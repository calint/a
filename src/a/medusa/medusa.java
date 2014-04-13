package a.medusa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

final public class medusa extends glob implements Serializable{
	public static String plugin="a.medusa.level.ctor";
	final public void reset()throws Throwable{
		players_all.clear();
		Class.forName(plugin).asSubclass(plugin.class).newInstance().init(this);
		players_free.clear();
		players_free.addAll(players_all);		
	}
	public long frame;
	final@Override public void tick(final float dt,final medusa m){
		last_update_dt_s=dt;
		frame++;
//		players_all.forEach(g->g.tick(dt));
		players_all.parallelStream().forEach(g->g.tick(dt,m));
	}
	final@Override public void draw(final screen s,final medusa m){
		s.clear('.');
		players_all.forEach(g->g.draw(s,m));
	}
	final public player alloc_player(){if(players_free.isEmpty())return null;return players_free.removeFirst();}
	final void on_closed_connection(final player p){players_free.add(p);}
	private final ArrayList<player>players_all=new ArrayList<>(128);
	final public medusa players_add(final@takes player p){players_all.add(p);return this;}
	final public player players_get(final int id){return players_all.get(id);}
	private final LinkedList<player>players_free=new LinkedList<>();
	final boolean has_active_players(){return players_free.size()==players_all.size();}
	private float last_update_dt_s;
	final public float dt(final float s){return s*last_update_dt_s;}
	public final static float dtor=(float)(Math.PI/180);
	public static float dtor(int degrees){return degrees*dtor;}
	
	public static @interface readonly{}
	public static @interface takes{}
	public static @interface gives{}
	public static interface plugin{void init(final medusa m)throws Throwable;}
	private static final long serialVersionUID=1L;
	final public int players_free_count(){return players_free.size();}
	final public int players_active_count(){return players_all.size()-players_free.size();}
}