package a.medusa;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class glob implements Serializable{
	public void draw(final screen s,final medusa m){
		if(glo==null)return;
		glo.draw_to_screen(s,phys.pos,phys.a);
	}
	public void tick(final float dt,final medusa m){
		phys.tick(dt);
	}
	public void on_msg(final ByteBuffer bb,final medusa mds)throws Throwable{}
	final public glob xy(final float x, final float y){phys.pos[0]=x;phys.pos[1]=y;return this;}
	final public glob glo(final glo g){glo=g;return this;}
	final public glob da(float radians){phys.da=radians;return this;}

	final protected physics phys=new physics();
	private glo glo;
	private static final long serialVersionUID=1;
}