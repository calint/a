package a.pzm;

import b.a;
import b.xwriter;

final public class line_numbers extends a{
	public int focus_line=0;
	@Override public void to(xwriter x) throws Throwable{
		for(int i=1;i<100;i++)
			if(i==focus_line) x.divo("","color:#800;font-weight:bold;background:yellow").p(Integer.toString(i)).div_();
			else x.pl(Integer.toString(i));
	}

	private static final long serialVersionUID=1;
}