package a.medusa;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class glob implements Serializable{
	public void draw(final screen s,final medusa m){
		if(glo==null)return;
		glo.draw_to_screen(s,physics.position,physics.angle);
	}
	public void tick(final float dt,final medusa m){
		physics.tick(dt);
	}
	public void on_msg(final ByteBuffer bb,final medusa mds)throws Throwable{}
	final public glob xy(final float x, final float y){physics.position[0]=x;physics.position[1]=y;return this;}
	final public glob glo(final glo g){glo=g;return this;}
	final public glob da(float radians){physics.dangle_over_dt=radians;return this;}

	final protected physics physics=new physics();
	final protected volume volume=new volume();
	private glo glo;
	
	static boolean check_collision(final glob a,final glob b){
		return false;
	}
	private static final long serialVersionUID=1;
}