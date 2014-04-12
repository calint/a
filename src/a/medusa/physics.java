package a.medusa;

import java.io.Serializable;

public class physics implements Serializable{
	final float[]pos=new float[]{0,0};
	final float[]dpos=new float[]{0,0};
	float a;
	float da;
	public void update(final float dt){
		glo.add2(pos,dpos,dt);
		a+=da*dt;
	}
	private static final long serialVersionUID = 1L;
}