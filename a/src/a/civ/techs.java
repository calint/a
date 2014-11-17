package a.civ;
import b.a;
import b.xwriter;
public class techs extends a{
	public void to(xwriter x)throws Throwable{
		x.div(this);
		x.pl("discovered technologies:");
		x.pl("sharp sticking");
		x.pl("rock throwing");
		x.pl("kicking");
		x.pl("hitting");
		x.pl("shouting");
		x.div_();
	}

	private static final long serialVersionUID=1;
}
