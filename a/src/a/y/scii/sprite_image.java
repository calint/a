package a.y.scii;

public class sprite_image{
	String[]scan_lines=new String[]{
			" ____ ",
			"|O  O|",
			"|_  _|",
			" /||\\ "
		};
	public int wi,hi;
	public void load(){
		hi=scan_lines.length;
		wi=scan_lines[0].length();
	}
	public void draw_to_screen(final screen s,final int x,final int y){
		final int h=s.hi;
		if(y>=h)return;
		int r=y,c=x;
		for(String ln:scan_lines){
			s.cursor_place(r++,c);
			if(r==h)break;
			s.print(ln);
		}
	}
}