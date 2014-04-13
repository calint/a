package a.medusa;

import java.nio.ByteBuffer;

public class player extends glob{
	@Override public void draw(final screen s,final medusa m){
		super.draw(s,m);
		if(label==null)return;
		s.render_text(label,phys.pos);
	}
	@Override public void tick(final float dt,final medusa m){
		super.tick(dt,m);
		phys.dpos[0]=speed*(float)Math.cos(a);
		phys.dpos[1]=speed*(float)Math.sin(a);
	}
	private static boolean has(final String s,final char ch){
		return s.indexOf(ch)!=-1;
	}
	@Override public void on_msg(final ByteBuffer bb,final medusa mds)throws Throwable{
//		System.out.println(this+" "+msg);
		final String msg=new String(bb.array(),bb.position(),bb.remaining(),"utf8");
		final char type=msg.charAt(0);
		if(type=='0'){
			final String s=msg.substring(1);
			final float d=speed_default;
			if(has(s,'W')){phys.dpos[1]=-d;}
			if(has(s,'A')){phys.dpos[0]=-d;}
			if(has(s,'S')){phys.dpos[1]=d;}
			if(has(s,'D')){phys.dpos[0]=d;}
			glo.normalize(phys.dpos,d);
		}else if(type=='3'){
			label=msg.substring(1);
//			System.out.println(label);
		}
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
	private String label;
	private float a;
	private float speed;
	private float speed_default=2;
	private float da=(float)(Math.PI*2/5);
	private static final long serialVersionUID=1;
}