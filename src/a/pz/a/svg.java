package a.pz.a;

import b.a;
import b.bin;
import b.xwriter;

public class svg extends a implements bin{
	@Override public String contenttype(){return"image/svg+xml";}
	@Override public void to(xwriter x) throws Throwable{
//		x.pl("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
		x.pl("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">");
		x.pl("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width='300px' height='300px'>");
		x.pl("<title>Small SVG example</title>");
		x.pl("<circle cx='120' cy='150' r='60' style='fill:gold'><animate attributeName='r' from='2' to='80' begin='0' dur='3' repeatCount='indefinite'/></circle>");
		x.pl("<polyline points='120 30, 25 150, 290 150' stroke-width='4' stroke='brown' style='fill:none'/>");
		x.pl("<polygon points='210 100, 210 200, 270 150' style='fill:lawngreen'/>");
		x.pl("<text x='60' y='250' fill='blue'>Hello, World!</text>");
		x.pl("</svg>");
	}
	private static final long serialVersionUID=1;
}
