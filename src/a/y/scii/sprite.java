package a.y.scii;
public class sprite{
	public sprite_image sprite_image;
	public float x,y;
	public void update(){}
	public void draw(final screen s){
		if(sprite_image==null)return;
		sprite_image.draw_to_screen(s,Math.round(x),Math.round(y));
	}
	public void on_msg(final String s)throws Throwable{
		if("a".equals(s)){
			x--;y++;
		}else if("d".equals(s)){
			x++;y++;
		}else if("w".equals(s)){
			y--;x--;
		}else if("s".equals(s)){
			y++;x--;
		}else if("e".equals(s)){
			y--;x++;
		}
	}
}