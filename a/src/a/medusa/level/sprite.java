package a.medusa.level;

import a.medusa.glo;
import a.medusa.medusa;
import a.medusa.screen;
import a.medusa.medusa.reads;

public class sprite implements glo{
	private String[]scan_lines=new String[]{
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
	@Override public void draw(final screen s,final@reads float[]xy,final float angle){
		final int h=s.hi;
		final int w=s.wi;
		final int x=(int)xy[0];
		final int y=(int)xy[1];
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