package a.medusa;


public class sprite extends glo{
	String[]scan_lines=new String[]{
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
	@Override public void draw_to_screen(final screen s,final/*readonly*/float[]xy,final float angle){
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
//		System.out.println(x+"  "+y+"   "+cut_left);
		for(String ln:scan_lines){
			s.cursor_place(r++,c);
			if(r<1)continue;
			if(r>=h)break;
			s.print(ln,cut_left);
		}
	}
	
	private static final long serialVersionUID = 1L;

}