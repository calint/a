package a.medusa;

import java.io.Serializable;
import java.nio.ByteBuffer;

import a.medusa.medusa.reads;
import a.medusa.medusa.takes;

public class glob implements Serializable{
	public void draw(final screen s,final medusa m){
		if(glo!=null)glo.draw(s,physics.position,physics.angle);
	}
	public void tick(final float dt,final medusa m){
		if(physics!=null)physics.tick(dt);
	}
	public void on_msg(final ByteBuffer bb,final medusa mds)throws Throwable{}
	final public glob xy(final float x, final float y){physics.position[0]=x;physics.position[1]=y;return this;}
	final public glob glo(final glo g){glo=g;return this;}
	final public glob da(float radians){physics.dangle_over_dt=radians;return this;}	
	static boolean check_collision(final glob a,final glob b){
		return false;
	}
	
	// components add by subclasses
	protected@takes physics physics;
	protected@takes volume volume;
	protected@reads glo glo;

	private static final long serialVersionUID=1;
}