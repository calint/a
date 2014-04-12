package a.medusa;
public class player extends glob{
	@Override public void tick(final float dt){
		super.tick(dt);
		phys.dpos[0]=speed*(float)Math.cos(a);
		phys.dpos[1]=speed*(float)Math.sin(a);
	}
	private static boolean has(final String s,final char ch){
		return s.indexOf(ch)!=-1;
	}
	@Override public void on_msg(final String s,final medusa mds)throws Throwable{
//		System.out.println(this+" "+s);
		final float d=speed_default;
		if(has(s,'W')){phys.dpos[1]=-d;}
		if(has(s,'A')){phys.dpos[0]=-d;}
		if(has(s,'S')){phys.dpos[1]=d;}
		if(has(s,'D')){phys.dpos[0]=d;}
//		System.out.println(phys.dpos[0]+"  "+phys.dpos[1]);
		glo.normalize(phys.dpos,d);
//		System.out.println(phys.dpos[0]+"  "+phys.dpos[1]);

//		final float dt=mds.dt(1);
//		final float add=d*dt;
		
//		if("e".equals(s)){
//			phys.pos[0]+=add;phys.pos[1]-=add;
//		}else if("q".equals(s)){
//			phys.pos[0]-=add;phys.pos[1]-=add;
//		}else if("z".equals(s)){
//			phys.pos[0]-=add;phys.pos[1]+=add;
//		}else if("x".equals(s)){
//			phys.pos[1]+=add;
//		}else if("c".equals(s)){
//			phys.pos[0]+=add;phys.pos[1]+=add;
//		}else if(" ".equals(s)){
//			phys.dpos[0]=phys.dpos[1]=0;
//		}else if("o".equals(s)){
//			a-=da*dt;
//		}else if("p".equals(s)){
//			a+=da*dt;
//		}else if("i".equals(s)){
//			sp=2;
//		}else if("j".equals(s)){
//			sp=0;
//		}
	}
	private float a;
	private float speed;
	private float speed_default=2;
	private float da=(float)(Math.PI*2/5);
	private static final long serialVersionUID=1;
}