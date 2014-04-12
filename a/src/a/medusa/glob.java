package a.medusa;

import java.io.Serializable;

public class glob implements Serializable{
	public void draw(final screen s){
		if(glo==null)return;
		glo.draw_to_screen(s,phys.pos,phys.a);
	}
	public void update(final float dt){
		phys.update(dt);
	}
	public void on_msg(final String s,final medusa mds)throws Throwable{
//		System.out.println("message to "+this+": "+s);
	}
	public glob xy(final float x, final float y){phys.pos[0]=x;phys.pos[1]=y;return this;}
	public glob glo(final glo g){glo=g;return this;}
	public glob da(float radians){phys.da=radians;return this;}

	protected final physics phys=new physics();
	private glo glo;
	private static final long serialVersionUID=1;
}