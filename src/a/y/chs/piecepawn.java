package a.y.chs;
import b.xwriter;
public class piecepawn extends piece{
	private static String[] pieces={"45px-Chess_pdt45.svg.png","45px-Chess_plt45.svg.png"};
	private static final long serialVersionUID=1L;
	public piecepawn(final int color){super(color);}
	public void to(final xwriter x)throws Throwable{x.p("<img src=\"").p("/chs/").p(pieces[color]).p("\">");}
}
