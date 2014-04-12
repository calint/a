package a.medusa;

import java.io.Serializable;

public class glob implements Serializable{
	public void update(final float dt){phys.update(dt);}
	public void draw(final screen s){}
	public void on_msg(final String s,final medusa mds)throws Throwable{}
	public glob xy(final float x, final float y){phys.pos[0]=x;phys.pos[1]=y;return this;}

	protected final physics phys=new physics();
	private static final long serialVersionUID=1;
}