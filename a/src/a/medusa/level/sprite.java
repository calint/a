package a.medusa.level;

import a.medusa.glo;
import a.medusa.algebra.point;
import a.medusa.medusa.reads;
import a.medusa.screen;

public class sprite implements glo{
	private String[]scan_lines={
			" ____ ",
			"|O  O|",
			"|_  _|",
			" /||\\ "
		};
	public int wi,hi;
	@Override public void load(){
		hi=scan_lines.length;
		wi=scan_lines[0].length();
	}
	@Override public void draw(final screen s,final@reads point p,final float angle){
		final int h=s.hi;
		final int w=s.wi;
		final int x=(int)p.x;
		final int y=(int)p.y;
		if(x>w)return;
		if(x+wi<0)return;
		if(y>=h)return;
		if(y+hi<0)return;
		int r=y,c=x;
		int cut_left=0;
		if(c<0){
			cut_left=-c;
			c=0;
		}
		for(final String ln:scan_lines){
			s.cursor_place(r++,c);
			if(r<1)continue;
			if(r>=h)break;
			s.print(ln,cut_left);
		}
	}
	
	private static final long serialVersionUID = 1L;
}