package a.y.scii;

import java.io.Serializable;

public class sprite implements Serializable{
	image image;
//	public float x,y;
	public void update(final float dt){
//		final float d=2;
		phys.dpos[0]=sp*(float)Math.cos(a);
		phys.dpos[1]=sp*(float)Math.sin(a);
		phys.update(dt);
	}
	public void draw(final screen s){
		if(image==null)return;
		image.draw_to_screen(s,phys.pos);
	}
	float d=2;
	float a;
	float sp;
	float da=(float)(Math.PI*2/5);
	public void on_msg(final String s,final float dt)throws Throwable{
		final float add=d*dt;
		if("a".equals(s)){phys.dpos[0]=-d;phys.dpos[1]=0;}
		else if("d".equals(s)){phys.dpos[0]=d;phys.dpos[1]=0;}
		else if("w".equals(s)){phys.dpos[1]=-d;phys.dpos[0]=0;}
		else if("s".equals(s)){phys.dpos[1]=d;phys.dpos[0]=0;}
		else if("e".equals(s)){
			phys.pos[0]+=add;phys.pos[1]-=add;
		}else if("q".equals(s)){
			phys.pos[0]-=add;phys.pos[1]-=add;
		}else if("z".equals(s)){
			phys.pos[0]-=add;phys.pos[1]+=add;
		}else if("x".equals(s)){
			phys.pos[1]+=add;
		}else if("c".equals(s)){
			phys.pos[0]+=add;phys.pos[1]+=add;
		}else if(" ".equals(s)){
			phys.dpos[0]=phys.dpos[1]=0;
		}else if("o".equals(s)){
			a-=da*dt;
		}else if("p".equals(s)){
			a+=da*dt;
		}else if("i".equals(s)){
			sp=2;
		}else if("j".equals(s)){
			sp=0;
		}
	}
	final physics phys=new physics();
	public static class physics implements Serializable{
		final float[]pos=new float[]{0,0};
		final float[]dpos=new float[]{0,0};
		public void update(final float dt){
			medusa.add2(pos,dpos,dt);
		}
		private static final long serialVersionUID = 1L;
	}
	public sprite xy(final float x,final float y){
		phys.pos[0]=x;phys.pos[1]=y;
		return this;
	}
	public sprite image(final image i){image=i;return this;}
	
	
	
	
	private static final long serialVersionUID = 1L;
}