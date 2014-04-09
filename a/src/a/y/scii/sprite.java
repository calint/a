package a.y.scii;

import java.io.Serializable;

public class sprite implements Serializable{
	image image;
//	public float x,y;
	public void update(){
		phys.update();
	}
	public void draw(final screen s){
		if(image==null)return;
		image.draw_to_screen(s,phys.pos);
	}
	public void on_msg(final String s)throws Throwable{
		if("a".equals(s)){
			phys.pos[0]--;phys.pos[1]++;
//			x--;y++;
		}else if("d".equals(s)){
			phys.pos[0]++;phys.pos[1]++;
//			x++;y++;
		}else if("w".equals(s)){
			phys.pos[0]--;phys.pos[1]--;
//			y--;x--;
		}else if("s".equals(s)){
			phys.pos[0]--;phys.pos[1]++;
//			y++;x--;
		}else if("e".equals(s)){
			phys.pos[0]++;phys.pos[1]--;
//			y--;x++;
		}
	}
	final physics phys=new physics();
	public static class physics implements Serializable{
		final float[]pos=new float[]{0,0};
		final float[]dpos=new float[]{0,0};
		public void update(){
			medusa.add2(pos,dpos,medusa.dt);
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