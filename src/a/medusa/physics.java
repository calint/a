package a.medusa;

import java.io.Serializable;

public class physics implements Serializable{
	final float[]pos=new float[]{0,0};
	final float[]dpos=new float[]{0,0};
	public void update(final float dt){
		medusa.add2(pos,dpos,dt);
	}
	private static final long serialVersionUID = 1L;
}