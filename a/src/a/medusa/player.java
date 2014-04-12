package a.medusa;
public class player extends glob{
	@Override public void update(final float dt){
		super.update(dt);
		phys.dpos[0]=sp*(float)Math.cos(a);
		phys.dpos[1]=sp*(float)Math.sin(a);
	}
	@Override public void on_msg(final String s,final medusa mds)throws Throwable{
		final float dt=mds.last_update_dt_s;
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
	private float d=2;
	float a;
	protected float sp;
	float da=(float)(Math.PI*2/5);
	private static final long serialVersionUID=1;
}